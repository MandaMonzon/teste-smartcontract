package org.blockchain.epcis.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * EpcisEvent (Classe Base)
 * 
 * Representa um evento EPCIS 2.0 conforme padrão GS1.
 * Contém campos comuns para rastreabilidade de medicamentos.
 * 
 * Métricas de Latency: Este objeto é serializado/desserializado via Jackson.
 * Tempo esperado: ~5-10ms para evento mediano.
 */
@DataType
public abstract class EpcisEvent {
    
    @Property
    @JsonProperty("eventID")
    protected String eventID;  // URN único (ex: urn:uuid:12345678-1234-5678-1234-567812345678)
    
    @Property
    @JsonProperty("eventTime")
    protected String eventTime;  // ISO 8601 (ex: 2026-04-29T21:42:00Z)
    
    @Property
    @JsonProperty("recordTime")
    protected String recordTime;  // Quando foi registrado no Ledger
    
    @Property
    @JsonProperty("bizStep")
    protected String bizStep;  // Capturing, Shipping, Receiving, Accepting, Storing
    
    @Property
    @JsonProperty("disposition")
    protected String disposition;  // Observed, Incomplete, Sellable_Returnable, etc
    
    @Property
    @JsonProperty("readPoint")
    protected String readPoint;  // Localização de leitura (ex: urn:epc:id:sgln:warehouse01)
    
    @Property
    @JsonProperty("bizLocation")
    protected String bizLocation;  // Localização de negócio (ex: urn:epc:id:sgln:distributor02)
    
    @Property
    @JsonProperty("certificationInfo")
    protected String certificationInfo;  // URL com certificado/assinatura digital
    
    @Property
    @JsonProperty("eventStatus")
    protected String eventStatus;  // ACTIVE, INVALIDATED, SUSPICIOUS_LOCATION, COMPROMISED
    
    @Property
    @JsonProperty("extensoes")
    protected Map<String, String> extensoes = new HashMap<>();  // Para ext1:, example:, etc
    
    // Getters e Setters
    public String getEventID() { return eventID; }
    public void setEventID(String eventID) { this.eventID = eventID; }
    
    public String getEventTime() { return eventTime; }
    public void setEventTime(String eventTime) { this.eventTime = eventTime; }
    
    public String getRecordTime() { return recordTime; }
    public void setRecordTime(String recordTime) { this.recordTime = recordTime; }
    
    public String getBizStep() { return bizStep; }
    public void setBizStep(String bizStep) { this.bizStep = bizStep; }
    
    public String getDisposition() { return disposition; }
    public void setDisposition(String disposition) { this.disposition = disposition; }
    
    public String getReadPoint() { return readPoint; }
    public void setReadPoint(String readPoint) { this.readPoint = readPoint; }
    
    public String getBizLocation() { return bizLocation; }
    public void setBizLocation(String bizLocation) { this.bizLocation = bizLocation; }
    
    public String getCertificationInfo() { return certificationInfo; }
    public void setCertificationInfo(String certificationInfo) { this.certificationInfo = certificationInfo; }
    
    public String getEventStatus() { return eventStatus; }
    public void setEventStatus(String eventStatus) { this.eventStatus = eventStatus; }
    
    public Map<String, String> getExtensoes() { return extensoes; }
    public void setExtensoes(Map<String, String> extensoes) { this.extensoes = extensoes; }
    
    public abstract String getEventType();
}
