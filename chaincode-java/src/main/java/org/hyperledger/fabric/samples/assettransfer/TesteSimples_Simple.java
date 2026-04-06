package org.hyperledger.fabric.samples.assettransfer;

import java.util.HashMap;
import java.util.Map;
import org.hyperledger.fabric.chaincode.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ResponseUtils;
import org.hyperledger.fabric.shim.response.Response;

/**
 * Versão ultra-simples de referência
 */
public class TesteSimples_Simple extends ChaincodeBase {

    private static final Map<String, String> db = new HashMap<>();

    @Override
    public Response init(ChaincodeStub stub) {
        return ResponseUtils.newSuccessResponse("OK");
    }

    @Override
    public Response invoke(ChaincodeStub stub) {
        String func = stub.getFunction();

        if ("set".equals(func)) {
            String key = stub.getParameters().get(0);
            String value = stub.getParameters().get(1);
            db.put(key, value);
            return ResponseUtils.newSuccessResponse("OK");
        }

        if ("get".equals(func)) {
            String key = stub.getParameters().get(0);
            String value = db.get(key);
            return ResponseUtils.newSuccessResponse(value != null ? value : "");
        }

        return ResponseUtils.newErrorResponse("Função inválida");
    }

    public static void main(String[] args) {
        new TesteSimples_Simple().start(args);
    }
}
