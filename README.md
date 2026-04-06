# TesteSimples - Smart Contract Hyperledger Fabric

Contrato minimalista com operações registrar/consultar para prototipagem em Blockchain.

**Compilar:**
```bash
cd chaincode-java && mvn clean package
```

**Deployar no test-network:**
```bash
./network.sh deployCC
```

**Invocar registrar:**
```bash
peer chaincode invoke -C mychannel -n testeSimples -c '{"function":"registrar","Args":["id1","2026-04-06","12345678900","/local","admin"]}'
```

**Consultar:**
```bash
peer chaincode query -C mychannel -n testeSimples -c '{"function":"consultar","Args":["id1"]}'
```
