package org.hyperledger.fabric.samples.assettransfer.contract;

import java.time.Instant;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.contract.annotation.Transaction.TYPE;
import org.hyperledger.fabric.shim.ChaincodeStub;

import org.hyperledger.fabric.samples.assettransfer.utils.EpcisParser;
import org.hyperledger.fabric.samples.assettransfer.models.*;

/**
 * EpcisContract - Smart Contract para Rastreabilidade de Medicamentos EPCIS 2.0.
 * 
 * Implementa transações conforme padrão GS1 EPCIS:
 * - registrarAgregacao: Salva eventos de agregação (pallet com produtos)
 * - registrarAssociacao: Salva eventos de associação (equipamento com sensores)
 * - processarDeclaracaoErro: Marca eventos como INVALIDATED e vincula ao correto
 * - consultarCadeiaCustodia: Resolve hierarquia completa de um produto
 * - consultarEvento: Busca um evento específico
 * 
 * @contract EpcisContract
 * @author Arquiteto de Blockchain
 * @version 2.0
 */
@Contract(
    name = "EpcisContract",
    info = @Info(
        title = "Rastreabilidade de Medicamentos EPCIS 2.0",
        description = "Smart Contract para blockchain de medicamentos com suporte XML/JSON",
        version = "2.0"
    )
)
public class EpcisContract implements ContractInterface {
    
    private static final ObjectMapper mapper = new ObjectMapper();
    
    /**
     * registrarAgregacao - Registra um evento de agregação (pallet com produtos).
     * 
     * Aceita payload em XML ou JSON. O parser detecta e normaliza automaticamente.
     * Salva no World State com eventID como chave.
     * 
     * @param ctx Contexto da transação
     * @param payload XML ou JSON com dados do evento
     * @return Mensagem de sucesso com eventID
     * @throws Exception se eventID duplicado ou payload inválido
     */
    @Transaction(intent = TYPE.SUBMIT)
    public String registrarAgregacao(Context ctx, String payload) throws Exception {
        ChaincodeStub stub = ctx.getStub();
        
        // Parsear e normalizar para JSON
        JsonNode eventoNode = EpcisParser.parseEvento(payload);
        
        // Validar campos obrigatórios
        if (!eventoNode.has("eventID")) {
            throw new IllegalArgumentException("eventID é obrigatório");
        }
        if (!eventoNode.has("parentID")) {
            throw new IllegalArgumentException("parentID (SSCC) é obrigatório");
        }
        
        String eventID = eventoNode.get("eventID").asText();
        String parentID = eventoNode.get("parentID").asText();
        
        // Verificar duplicidade
        byte[] existente = stub.getState(eventID);
        if (existente != null && existente.length > 0) {
            throw new IllegalArgumentException("Evento já existe: " + eventID);
        }
        
        // Adicionar metadados do blockchain
        ((com.fasterxml.jackson.databind.node.ObjectNode) eventoNode)
            .put("dataRegistroBlockchain", Instant.now().toString());
        ((com.fasterxml.jackson.databind.node.ObjectNode) eventoNode)
            .put("status", "ATIVO");
        ((com.fasterxml.jackson.databind.node.ObjectNode) eventoNode)
            .put("tipoEvento", "AGGREGATION");
        
        // Salvar no World State
        String eventoJSON = EpcisParser.serializarParaJSON(eventoNode);
        stub.putState(eventID, eventoJSON.getBytes("UTF-8"));
        
        // Manter histórico vinculado ao parentID (para consultarCadeiaCustodia)
        atualizarHistorico(stub, parentID, eventID);
        
        return String.format("✓ Agregação registrada%n  EventID: %s%n  Pallet: %s%n  Status: ATIVO", 
            eventID, parentID);
    }
    
    /**
     * registrarAssociacao - Registra um evento de associação (equipamento com sensores).
     * 
     * Exemplo: Vincular um refrigerador a seus sensores de temperatura.
     * 
     * @param ctx Contexto da transação
     * @param payload XML ou JSON com dados do evento
     * @return Mensagem de sucesso
     * @throws Exception se inválido
     */
    @Transaction(intent = TYPE.SUBMIT)
    public String registrarAssociacao(Context ctx, String payload) throws Exception {
        ChaincodeStub stub = ctx.getStub();
        
        JsonNode eventoNode = EpcisParser.parseEvento(payload);
        
        if (!eventoNode.has("eventID")) {
            throw new IllegalArgumentException("eventID é obrigatório");
        }
        if (!eventoNode.has("parentID")) {
            throw new IllegalArgumentException("parentID é obrigatório");
        }
        if (!eventoNode.has("action")) {
            throw new IllegalArgumentException("action (ADD/DELETE/OBSERVE) é obrigatória");
        }
        
        String action = eventoNode.get("action").asText();
        if (!action.matches("ADD|DELETE|OBSERVE")) {
            throw new IllegalArgumentException("Action inválida: " + action);
        }
        
        String eventID = eventoNode.get("eventID").asText();
        
        byte[] existente = stub.getState(eventID);
        if (existente != null && existente.length > 0) {
            throw new IllegalArgumentException("Evento já existe: " + eventID);
        }
        
        ((com.fasterxml.jackson.databind.node.ObjectNode) eventoNode)
            .put("dataRegistroBlockchain", Instant.now().toString());
        ((com.fasterxml.jackson.databind.node.ObjectNode) eventoNode)
            .put("status", "ATIVO");
        ((com.fasterxml.jackson.databind.node.ObjectNode) eventoNode)
            .put("tipoEvento", "ASSOCIATION");
        
        String eventoJSON = EpcisParser.serializarParaJSON(eventoNode);
        stub.putState(eventID, eventoJSON.getBytes("UTF-8"));
        
        return String.format("✓ Associação registrada%n  EventID: %s%n  Ação: %s", 
            eventID, action);
    }
    
    /**
     * processarDeclaracaoErro - Marca um evento original como INVALIDADO.
     * 
     * Implementa o conceito EPCIS de Corrective Event.
     * Nada é deletado - tudo fica no blockchain para auditoria completa.
     * 
     * @param ctx Contexto da transação
     * @param eventIDOriginal ID do evento com erro
     * @param correctiveEventID ID do evento corrigido
     * @param tipoErro Tipo de erro (DATA_ERROR, PRODUCT_ERROR, etc)
     * @return Mensagem confirmando a correção
     * @throws Exception se evento original não existir
     */
    @Transaction(intent = TYPE.SUBMIT)
    public String processarDeclaracaoErro(Context ctx, String eventIDOriginal, 
                                         String correctiveEventID, String tipoErro) throws Exception {
        ChaincodeStub stub = ctx.getStub();
        
        byte[] eventoOriginalBytes = stub.getState(eventIDOriginal);
        if (eventoOriginalBytes == null || eventoOriginalBytes.length == 0) {
            throw new IllegalArgumentException("Evento original não encontrado: " + eventIDOriginal);
        }
        
        // Marcar original como INVALIDADO
        JsonNode eventoOriginal = mapper.readTree(eventoOriginalBytes);
        ((com.fasterxml.jackson.databind.node.ObjectNode) eventoOriginal)
            .put("status", "INVALIDADO");
        ((com.fasterxml.jackson.databind.node.ObjectNode) eventoOriginal)
            .put("correctiveEventID", correctiveEventID);
        ((com.fasterxml.jackson.databind.node.ObjectNode) eventoOriginal)
            .put("tipoErro", tipoErro);
        ((com.fasterxml.jackson.databind.node.ObjectNode) eventoOriginal)
            .put("dataCorrecao", Instant.now().toString());
        
        stub.putState(eventIDOriginal, mapper.writeValueAsBytes(eventoOriginal));
        
        return String.format(
            "✓ Erro processado%n" +
            "  Evento inválido: %s%n" +
            "  Evento correto: %s%n" +
            "  Tipo: %s%n" +
            "  Auditoria completa preservada no blockchain.",
            eventIDOriginal, correctiveEventID, tipoErro);
    }
    
    /**
     * consultarCadeiaCustodia - Recupera toda a cadeia de custódia de um produto.
     * 
     * Resolve hierarquia: se buscar um medicamento que foi agregado a um pallet,
     * retorna também os eventos do pallet.
     * 
     * @param ctx Contexto da transação
     * @param epcOuSSCC EPC do medicamento ou SSCC do pallet
     * @return JSON com histórico completo
     * @throws Exception se não encontrar histórico
     */
    @Transaction(intent = TYPE.EVALUATE)
    public String consultarCadeiaCustodia(Context ctx, String epcOuSSCC) throws Exception {
        ChaincodeStub stub = ctx.getStub();
        
        String chaveHistorico = "hist_" + epcOuSSCC;
        byte[] historicoBytes = stub.getState(chaveHistorico);
        
        if (historicoBytes == null || historicoBytes.length == 0) {
            throw new IllegalArgumentException("Nenhum histórico encontrado para: " + epcOuSSCC);
        }
        
        List<String> eventosIDs = mapper.readValue(historicoBytes,
            mapper.getTypeFactory().constructCollectionType(List.class, String.class));
        
        Map<String, Object> resposta = new LinkedHashMap<>();
        resposta.put("epcOuSSCC", epcOuSSCC);
        resposta.put("totalEventos", eventosIDs.size());
        resposta.put("dataConsulta", Instant.now().toString());
        
        List<Map<String, Object>> eventos = new ArrayList<>();
        
        for (String eventID : eventosIDs) {
            byte[] eventoBytes = stub.getState(eventID);
            if (eventoBytes != null) {
                JsonNode evento = mapper.readTree(eventoBytes);
                Map<String, Object> info = new LinkedHashMap<>();
                
                info.put("eventID", evento.get("eventID").asText());
                info.put("tipoEvento", evento.has("tipoEvento") ? 
                    evento.get("tipoEvento").asText() : "?");
                info.put("eventTime", evento.has("eventTime") ? 
                    evento.get("eventTime").asText() : "?");
                info.put("bizLocation", evento.has("bizLocation") ? 
                    evento.get("bizLocation").asText() : "?");
                info.put("status", evento.get("status").asText());
                
                // Se foi invalidado, mostrar vínculo ao correto
                if ("INVALIDADO".equals(evento.get("status").asText())) {
                    if (evento.has("correctiveEventID")) {
                        info.put("⚠ corrigidoPor", evento.get("correctiveEventID").asText());
                    }
                }
                
                eventos.add(info);
            }
        }
        
        resposta.put("eventos", eventos);
        
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(resposta);
    }
    
    /**
     * consultarEvento - Busca um evento específico pelo ID.
     * 
     * @param ctx Contexto da transação
     * @param eventID ID do evento
     * @return JSON com dados completo do evento
     * @throws Exception se evento não existir
     */
    @Transaction(intent = TYPE.EVALUATE)
    public String consultarEvento(Context ctx, String eventID) throws Exception {
        ChaincodeStub stub = ctx.getStub();
        
        byte[] eventoBytes = stub.getState(eventID);
        if (eventoBytes == null || eventoBytes.length == 0) {
            throw new IllegalArgumentException("Evento não encontrado: " + eventID);
        }
        
        JsonNode evento = mapper.readTree(eventoBytes);
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(evento);
    }
    
    // ========================================================================
    // MÉTODOS AUXILIARES
    // ========================================================================
    
    /**
     * Atualiza histórico de um produto/pallet adicionando um novo eventID.
     * 
     * @param stub ChaincodeStub
     * @param chave EPC ou SSCC
     * @param eventID ID do evento a registrar
     * @throws Exception se houver erro
     */
    private void atualizarHistorico(ChaincodeStub stub, String chave, String eventID) throws Exception {
        String chaveHistorico = "hist_" + chave;
        byte[] historicoBytes = stub.getState(chaveHistorico);
        List<String> eventos = new ArrayList<>();
        
        if (historicoBytes != null && historicoBytes.length > 0) {
            eventos = mapper.readValue(historicoBytes,
                mapper.getTypeFactory().constructCollectionType(List.class, String.class));
        }
        
        eventos.add(eventID);
        stub.putState(chaveHistorico, mapper.writeValueAsBytes(eventos));
    }
}
