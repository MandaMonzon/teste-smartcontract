package org.hyperledger.fabric.samples.assettransfer.models;

import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

/**
 * QuantidadeItem - Representa quantidade de um produto em um evento de agregação.
 * Segue padrão EPCIS: epcClass + quantidade decimal + UOM (unidade de medida).
 * 
 * Exemplo: 100 unidades (EA) do medicamento LOTE-2026-04
 */
@DataType()
public class QuantidadeItem {
    
    @Property()
    @JsonProperty("epcClass")
    private String epcClass;  // URN do produto (ex: urn:epc:class:sgtin:...)
    
    @Property()
    @JsonProperty("quantidade")
    private Double quantidade;  // Ex: 100.5 (suporta decimais)
    
    @Property()
    @JsonProperty("uom")
    private String uom;  // Unit of Measure: EA (each), KGM (kg), L (litro), etc.
    
    public QuantidadeItem() {
    }
    
    public QuantidadeItem(String epcClass, Double quantidade, String uom) {
        this.epcClass = epcClass;
        this.quantidade = quantidade;
        this.uom = uom;
    }
    
    public String getEpcClass() {
        return epcClass;
    }
    
    public void setEpcClass(String epcClass) {
        this.epcClass = epcClass;
    }
    
    public Double getQuantidade() {
        return quantidade;
    }
    
    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }
    
    public String getUom() {
        return uom;
    }
    
    public void setUom(String uom) {
        this.uom = uom;
    }
}
