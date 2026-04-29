package org.hyperledger.fabric.samples.assettransfer.utils;

import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * EpcisParser - "Ponte" multiformato entre XML e JSON para eventos EPCIS 2.0.
 * 
 * Responsabilidades:
 * 1. Detectar se o payload é XML (?<?xml> ou <epcis>) ou JSON ({})
 * 2. Converter XML para JSON usando Jackson XmlMapper
 * 3. Ignorar tags customizadas (ext1:, ext2:, etc) sem falhar
 * 4. Garantir que a estrutura seja normalizada para o World State do blockchain
 * 
 * Nota: A conversão XML->JSON é crítica para otimizar indexação no CouchDB,
 * que trabalha melhor com JSON estruturado.
 * 
 * @author Arquiteto de Blockchain EPCIS
 * @version 2.0
 */
public class EpcisParser {
    
    private static final ObjectMapper jsonMapper = new ObjectMapper();
    private static final XmlMapper xmlMapper = new XmlMapper();
    
    static {
        // Configuração crítica: ignorar campos desconhecidos (extensões customizadas)
        jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    
    /**
     * Detecta e converte payload para JSON normalizado.
     * 
     * @param payload String contendo XML ou JSON
     * @return JsonNode normalizado pronto para salvar no World State
     * @throws IOException se houver erro de parsing
     */
    public static JsonNode parseEvento(String payload) throws IOException {
        if (payload == null || payload.trim().isEmpty()) {
            throw new IOException("Payload vazio");
        }
        
        String trimmed = payload.trim();
        
        // Detectar formato
        if (trimmed.startsWith("<?xml") || trimmed.startsWith("<epcis") || trimmed.startsWith("<")) {
            return parseXML(trimmed);
        } else if (trimmed.startsWith("{")) {
            return parseJSON(trimmed);
        } else {
            throw new IOException("Formato inválido. Esperado XML ou JSON.");
        }
    }
    
    /**
     * Parse XML para JsonNode.
     * Configuração FAIL_ON_UNKNOWN_PROPERTIES = false garante que extensões como ext1:,
     * example: sejam ignoradas graciosamente.
     * 
     * @param xmlPayload String contendo XML
     * @return JsonNode
     * @throws IOException se XML for inválido
     */
    private static JsonNode parseXML(String xmlPayload) throws IOException {
        try {
            JsonNode xmlTree = xmlMapper.readTree(xmlPayload.getBytes("UTF-8"));
            return xmlTree;
        } catch (Exception e) {
            throw new IOException("Erro ao fazer parse de XML: " + e.getMessage(), e);
        }
    }
    
    /**
     * Parse JSON para JsonNode.
     * 
     * @param jsonPayload String contendo JSON-LD
     * @return JsonNode
     * @throws IOException se JSON for inválido
     */
    private static JsonNode parseJSON(String jsonPayload) throws IOException {
        try {
            JsonNode jsonNode = jsonMapper.readTree(jsonPayload);
            return jsonNode;
        } catch (Exception e) {
            throw new IOException("Erro ao fazer parse de JSON: " + e.getMessage(), e);
        }
    }
    
    /**
     * Serializa JsonNode para String JSON formatada.
     * Útil para salvar no World State com semântica legível.
     * 
     * @param node JsonNode a serializar
     * @return String JSON formatada
     * @throws IOException se houver erro
     */
    public static String serializarParaJSON(JsonNode node) throws IOException {
        return jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
    }
    
    /**
     * Valida se JsonNode contém campos críticos de um evento EPCIS.
     * 
     * @param node JsonNode do evento
     * @return true se válido
     */
    public static boolean validarEventoEPCIS(JsonNode node) {
        return node.has("eventID") && node.has("eventTime");
    }
}
