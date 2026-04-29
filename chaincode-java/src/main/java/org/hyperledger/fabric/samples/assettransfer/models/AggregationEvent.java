package org.hyperledger.fabric.samples.assettransfer.models;

import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

/**
 * AggregationEvent - Evento de agregação conforme EPCIS 2.0.
 * 
 * Gerencia hierarquia logística: um parentID (SSCC de pallet/caixa) contém
 * múltiplos produtos filhos (childEPCs ou childQuantityList).
 * 
 * Exemplo de uso acadêmico:
 * - parentID: "urn:sscc:1234567890123"  (pallet)
 * - childQuantityList: 100x medicamento LOTE-2026-04
 * - action: "ADD"
 * - temperatura: "2-8°C"  (condição de armazenamento)
 * 
 * @see AssociationEvent para associação entre equipamentos e sensores
 */
@DataType()
public class AggregationEvent extends EventoEPCIS {
    
    @Property()
    @JsonProperty("tipoEvento")
    private String tipoEvento = "AGGREGATION";
    
    @Property()
    @JsonProperty("parentID")
    private String parentID;  // SSCC: Serial Shipping Container Code
    
    @Property()
    @JsonProperty("childEPCs")
    private List<String> childEPCs = new ArrayList<>();  // EPCs individuais
    
    @Property()
    @JsonProperty("childQuantityList")
    private List<QuantidadeItem> childQuantityList = new ArrayList<>();  // Itens com quantidade
    
    @Property()
    @JsonProperty("action")
    private String action;  // ADD, DELETE ou OBSERVE
    
    @Property()
    @JsonProperty("loteID")
    private String loteID;  // Identificação do lote
    
    @Property()
    @JsonProperty("dataValidade")
    private String dataValidade;  // ISO 8601
    
    @Property()
    @JsonProperty("condicaoArmazenamento")
    private String condicaoArmazenamento;  // "2-8°C", "20-25°C", etc
    
    public AggregationEvent() {
        super();
    }
    
    @Override
    public String obterTipoEvento() {
        return "AGGREGATION";
    }
    
    public String getParentID() {
        return parentID;
    }
    
    public void setParentID(String parentID) {
        this.parentID = parentID;
    }
    
    public List<String> getChildEPCs() {
        return childEPCs;
    }
    
    public void setChildEPCs(List<String> childEPCs) {
        this.childEPCs = childEPCs;
    }
    
    public List<QuantidadeItem> getChildQuantityList() {
        return childQuantityList;
    }
    
    public void setChildQuantityList(List<QuantidadeItem> childQuantityList) {
        this.childQuantityList = childQuantityList;
    }
    
    public void adicionarItem(QuantidadeItem item) {
        childQuantityList.add(item);
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        if (!action.matches("ADD|DELETE|OBSERVE")) {
            throw new IllegalArgumentException("Action inválida: " + action);
        }
        this.action = action;
    }
    
    public String getLoteID() {
        return loteID;
    }
    
    public void setLoteID(String loteID) {
        this.loteID = loteID;
    }
    
    public String getDataValidade() {
        return dataValidade;
    }
    
    public void setDataValidade(String dataValidade) {
        this.dataValidade = dataValidade;
    }
    
    public String getCondicaoArmazenamento() {
        return condicaoArmazenamento;
    }
    
    public void setCondicaoArmazenamento(String condicaoArmazenamento) {
        this.condicaoArmazenamento = condicaoArmazenamento;
    }
}
