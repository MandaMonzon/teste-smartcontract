package org.hyperledger.fabric.samples.assettransfer.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

/**
 * SensorElement - Representa dados de telemetria/sensores conforme EPCIS 2.0.
 * 
 * Mapeia leituras de sensores: temperatura, umidade, luz, pressão, etc.
 * Inclui valores mínimo, máximo e média com unidade de medida padronizada.
 * 
 * Exemplo: Temperatura entre 2°C e 8°C com média de 5°C durante transporte.
 */
@DataType()
public class SensorElement {
    
    @Property()
    @JsonProperty("sensorType")
    private String sensorType;  // "Temperature", "AbsoluteHumidity", "Light", etc
    
    @Property()
    @JsonProperty("minValue")
    private Double minValue;
    
    @Property()
    @JsonProperty("maxValue")
    private Double maxValue;
    
    @Property()
    @JsonProperty("meanValue")
    private Double meanValue;
    
    @Property()
    @JsonProperty("uom")
    private String uom;  // CEL (Celsius), A93 (%), LUX, etc
    
    @Property()
    @JsonProperty("timestamp")
    private String timestamp;  // ISO 8601
    
    public SensorElement() {
    }
    
    public SensorElement(String sensorType, Double minValue, Double maxValue, 
                         Double meanValue, String uom, String timestamp) {
        this.sensorType = sensorType;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.meanValue = meanValue;
        this.uom = uom;
        this.timestamp = timestamp;
    }
    
    public String getSensorType() {
        return sensorType;
    }
    
    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }
    
    public Double getMinValue() {
        return minValue;
    }
    
    public void setMinValue(Double minValue) {
        this.minValue = minValue;
    }
    
    public Double getMaxValue() {
        return maxValue;
    }
    
    public void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }
    
    public Double getMeanValue() {
        return meanValue;
    }
    
    public void setMeanValue(Double meanValue) {
        this.meanValue = meanValue;
    }
    
    public String getUom() {
        return uom;
    }
    
    public void setUom(String uom) {
        this.uom = uom;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
