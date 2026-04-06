package org.hyperledger.fabric.samples.assettransfer;

import java.util.HashMap;
import java.util.Map;
import org.hyperledger.fabric.chaincode.ChaincodeBase;
import org.hyperledger.fabric.protos.peer.ChaincodeShim.ChaincodeMessage;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ResponseUtils;
import org.hyperledger.fabric.shim.response.Response;
import com.owlike.genson.Genson;

public class TesteSimples extends ChaincodeBase {

    private static final Genson genson = new Genson();
    private static final Map<String, String> database = new HashMap<>();

    @Override
    public Response init(ChaincodeStub stub) {
        return ResponseUtils.newSuccessResponse("Inicializado");
    }

    @Override
    public Response invoke(ChaincodeStub stub) {
        String funcName = stub.getFunction();
        String[] params = stub.getParameters().toArray(new String[0]);

        switch (funcName) {
            case "registrar":
                return registrar(stub, params);
            case "consultar":
                return consultar(stub, params);
            default:
                return ResponseUtils.newErrorResponse("Função desconhecida: " + funcName);
        }
    }

    private Response registrar(ChaincodeStub stub, String[] params) {
        if (params.length < 5) {
            return ResponseUtils.newErrorResponse("Erro: registrar requer 5 parâmetros");
        }

        String id = params[0];
        String hora = params[1];
        String cpf = params[2];
        String local = params[3];
        String responsavel = params[4];

        DadosTeste dados = new DadosTeste(hora, cpf, local, responsavel);
        String json = genson.serialize(dados);
        database.put(id, json);

        return ResponseUtils.newSuccessResponse("Registrado com ID: " + id);
    }

    private Response consultar(ChaincodeStub stub, String[] params) {
        if (params.length < 1) {
            return ResponseUtils.newErrorResponse("Erro: consultar requer 1 parâmetro");
        }

        String id = params[0];
        String json = database.get(id);

        if (json == null) {
            return ResponseUtils.newErrorResponse("Não encontrado: " + id);
        }

        return ResponseUtils.newSuccessResponse(json);
    }

    public static void main(String[] args) {
        new TesteSimples().start(args);
    }
}
