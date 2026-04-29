package org.blockchain.epcis.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

/**
 * SensorElement - Telemetria e evidência de integridade física
 * 
 * Suporta leitura de:
 * - Temperature (CEL, FAH)
 * - Humidity (A93 = Percent Relative Humidity)
 * - Tamper-proof sensors (indicador de violação física)
 * 
 * Crítico para 'Cargo Theft' e 'Medicine Counterfeit Mitigation':
 * Se isTampered = true, o medicamento deve ser marcado como COMPROMISED.
 */
@DataType
public class SensorElement {
    
    @Property
    @JsonProperty("sensorType")
    public String sensorType;  // Temperature, Humidity, Illuminance, etc
    
    @Property
    @JsonProperty("value")
    public Double value;  // Valor atual
    
    @Property
    @JsonProperty("minValue")
    public Double minValue;  // Mínimo registrado
    
    @Property
    @JsonProperty("maxValue")
    public Double maxValue;  // Máximo registrado
    
    @Property
    @JsonProperty("meanValue")
    public Double meanValue;  // Média
    
    @Property
    @JsonProperty("uom")
    public String uom;  // CEL (Celsius), A93 (Percent RH), etc
    
    @Property
    @JsonProperty("timestamp")
    public String timestamp;  // ISO 8601
    
    @Property
    @JsonProperty("isTampered")
    public Boolean isTampered = false;  // Sensor de violação física
    
    public SensorElement() {}
    
    public SensorElement(String sensorType, Double value, String uom, Boolean isTampered) {
        this.sensorType = sensorType;
        this.value = value;
        this.uom = uom;
        this.isTampered = isTampered;
    }
}
