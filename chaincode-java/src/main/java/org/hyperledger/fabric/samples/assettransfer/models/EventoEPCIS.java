package org.hyperledger.fabric.samples.assettransfer.models;

import java.util.Map;
import java.util.HashMap;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

/**
 * EventoEPCIS - Classe base abstrata para todos os eventos do padrão GS1 EPCIS 2.0
 * 
 * Campos comuns obrigatórios conforme especificação EPCIS:
 * - eventID: Identificador único (URN, URL ou Hash)
 * - eventTime: Data/hora em formato ISO 8601
 * - recordTime: Quando foi registrado no sistema
 * - bizStep: Passo da cadeia de negócios (ex: receiving, shipping)
 * - disposition: Estado do evento (ex: active, no_pedigree)
 * - readPoint: Localização do leitor RFID/sensor
 * - certificationInfo: URL com certificados/validações
 * 
 * @author Arquiteto de Blockchain EPCIS
 * @version 2.0
 */
@DataType()
public abstract class EventoEPCIS {
    
    @Property()
    @JsonProperty("eventID")
    private String eventID;
    
    @Property()
    @JsonProperty("eventTime")
    private String eventTime;
    
    @Property()
    @JsonProperty("recordTime")
    private String recordTime;
    
    @Property()
    @JsonProperty("bizStep")
    private String bizStep;
    
    @Property()
    @JsonProperty("disposition")
    private String disposition;
    
    @Property()
    @JsonProperty("readPoint")
    private String readPoint;
    
    @Property()
    @JsonProperty("certificationInfo")
    private String certificationInfo;
    
    @Property()
    @JsonProperty("status")
    private String status = "ATIVO";
    
    @Property()
    @JsonProperty("dataRegistroBlockchain")
    private String dataRegistroBlockchain;
    
    // Getters e Setters
    public String getEventID() {
        return eventID;
    }
    
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }
    
    public String getEventTime() {
        return eventTime;
    }
    
    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }
    
    public String getRecordTime() {
        return recordTime;
    }
    
    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }
    
    public String getBizStep() {
        return bizStep;
    }
    
    public void setBizStep(String bizStep) {
        this.bizStep = bizStep;
    }
    
    public String getDisposition() {
        return disposition;
    }
    
    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }
    
    public String getReadPoint() {
        return readPoint;
    }
    
    public void setReadPoint(String readPoint) {
        this.readPoint = readPoint;
    }
    
    public String getCertificationInfo() {
        return certificationInfo;
    }
    
    public void setCertificationInfo(String certificationInfo) {
        this.certificationInfo = certificationInfo;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getDataRegistroBlockchain() {
        return dataRegistroBlockchain;
    }
    
    public void setDataRegistroBlockchain(String dataRegistroBlockchain) {
        this.dataRegistroBlockchain = dataRegistroBlockchain;
    }
    
    /**
     * Retorna o tipo de evento (AGGREGATION, ASSOCIATION, etc)
     */
    public abstract String obterTipoEvento();
}
