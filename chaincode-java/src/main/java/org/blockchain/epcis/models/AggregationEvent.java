package org.blockchain.epcis.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import java.util.ArrayList;
import java.util.List;

/**
 * AggregationEvent - Hierarquia logística de medicamentos
 * 
 * Padrão GS1 para rastrear lotes e frações:
 * - parentID (SSCC): Identificador do pallet/caixa mãe
 * - childEPCs (SGTIN): Produtos individuais agregados
 * - childQuantityList: Itens com quantidade (para agregações heterogêneas)
 * 
 * SECURITY FEATURES (Secure Logistics):
 * 1. Geofencing Validation: latitude/longitude com validação de raio permitido
 * 2. Granularidade: Rastreia roubo tanto de container quanto de item fracionado
 * 3. Tamper-proof Evidence: Integrado via SensorElement
 * 
 * Throughput esperado: ~50-100 eventos/segundo em rede com 4 peers
 */
@DataType
public class AggregationEvent extends EpcisEvent {
    
    @Property
    @JsonProperty("tipoEvento")
    public String tipoEvento = "AGGREGATION";
    
    @Property
    @JsonProperty("parentID")
    public String parentID;  // SSCC do pallet/caixa-mãe
    
    @Property
    @JsonProperty("childEPCs")
    public List<String> childEPCs = new ArrayList<>();  // SGTIN dos produtos
    
    @Property
    @JsonProperty("childQuantityList")
    public List<QuantityElement> childQuantityList = new ArrayList<>();  // Itens com quantidade
    
    @Property
    @JsonProperty("action")
    public String action;  // ADD, DELETE, OBSERVE
    
    @Property
    @JsonProperty("batchLotNumber")
    public String batchLotNumber;  // Lote farmacêutico
    
    @Property
    @JsonProperty("dataValidade")
    public String dataValidade;  // Expiration date (ISO 8601)
    
    @Property
    @JsonProperty("temperaturaRecomendada")
    public String temperaturaRecomendada;  // Ex: 2-8°C
    
    // GEOFENCING VALIDATION
    @Property
    @JsonProperty("latitude")
    public Double latitude;  // Coordenada para validação geográfica
    
    @Property
    @JsonProperty("longitude")
    public Double longitude;  // Coordenada para validação geográfica
    
    @Property
    @JsonProperty("geofenceRadiusKm")
    public Double geofenceRadiusKm;  // Raio permitido em km
    
    @Property
    @JsonProperty("sensorElements")
    public List<SensorElement> sensorElements = new ArrayList<>();  // Evidência física
    
    @Override
    public String getEventType() {
        return "AGGREGATION";
    }
    
    public AggregationEvent() {}
    
    public AggregationEvent(String eventID, String parentID, String action) {
        this.eventID = eventID;
        this.parentID = parentID;
        this.action = action;
    }
}
