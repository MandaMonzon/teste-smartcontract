package org.hyperledger.fabric.samples.assettransfer.models;

import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

/**
 * AssociationEvent - Evento de associação conforme EPCIS 2.0.
 * 
 * Gerencia vínculos entre um ativo fixo (equipamento) e múltiplos itens filhos
 * (sensores, produtos, componentes).
 * 
 * Exemplo: Um refrigerador está vinculado a 3 sensores de temperatura.
 * 
 * @see AggregationEvent para agregação de produtos em pallets
 */
@DataType()
public class AssociationEvent extends EventoEPCIS {
    
    @Property()
    @JsonProperty("tipoEvento")
    private String tipoEvento = "ASSOCIATION";
    
    @Property()
    @JsonProperty("parentID")
    private String parentID;  // Ativo pai (ex: refrigerador, container fixo)
    
    @Property()
    @JsonProperty("childEPCs")
    private List<String> childEPCs = new ArrayList<>();  // Sensores ou produtos vinculados
    
    @Property()
    @JsonProperty("action")
    private String action;  // ADD, DELETE ou OBSERVE
    
    @Property()
    @JsonProperty("tipoAssociacao")
    private String tipoAssociacao;  // Ex: SENSOR_EQUIPAMENTO, PRODUTO_CONTAINER
    
    public AssociationEvent() {
        super();
    }
    
    @Override
    public String obterTipoEvento() {
        return "ASSOCIATION";
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
    
    public void adicionarChild(String epc) {
        childEPCs.add(epc);
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
    
    public String getTipoAssociacao() {
        return tipoAssociacao;
    }
    
    public void setTipoAssociacao(String tipoAssociacao) {
        this.tipoAssociacao = tipoAssociacao;
    }
}
