package org.blockchain.epcis.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import java.util.ArrayList;
import java.util.List;

/**
 * AssociationEvent - Vínculo entre equipamento e sensores/produtos
 * 
 * Exemplo: Um equipamento de refrigeração está associado a múltiplos sensores
 * de temperatura e umidade que monitoram a integridade da carga.
 * 
 * Usecase: Validar que sensores IoT (Tamper-proof) estão funcionando corretamente
 * durante o transporte de medicamentos.
 */
@DataType
public class AssociationEvent extends EpcisEvent {
    
    @Property
    @JsonProperty("tipoEvento")
    public String tipoEvento = "ASSOCIATION";
    
    @Property
    @JsonProperty("parentID")
    public String parentID;  // Equipamento pai (ex: container refrigerado)
    
    @Property
    @JsonProperty("childEPCs")
    public List<String> childEPCs = new ArrayList<>();  // Sensores ou produtos associados
    
    @Property
    @JsonProperty("action")
    public String action;  // ADD, DELETE, OBSERVE
    
    @Override
    public String getEventType() {
        return "ASSOCIATION";
    }
    
    public AssociationEvent() {}
    
    public AssociationEvent(String eventID, String parentID, String action) {
        this.eventID = eventID;
        this.parentID = parentID;
        this.action = action;
    }
}
