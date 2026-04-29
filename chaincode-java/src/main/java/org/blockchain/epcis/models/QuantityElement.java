package org.blockchain.epcis.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

/**
 * QuantityElement - Representa um item com quantidade em um AggregationEvent
 * 
 * Padrão GS1: EPC Class + Quantidade Decimal + UOM (Unit of Measure)
 * Exemplo: 100 unidades de medicamento SGTIN:12345678901234 em EA (Each)
 */
@DataType
public class QuantityElement {
    
    @Property
    @JsonProperty("epcClass")
    public String epcClass;  // URN do produto (ex: urn:epc:class:sgtin:...)
    
    @Property
    @JsonProperty("quantidade")
    public Double quantidade;  // Valor decimal
    
    @Property
    @JsonProperty("uom")
    public String uom;  // EA, KGM, LTR, CMK, etc.
    
    public QuantityElement() {}
    
    public QuantityElement(String epcClass, Double quantidade, String uom) {
        this.epcClass = epcClass;
        this.quantidade = quantidade;
        this.uom = uom;
    }
}
