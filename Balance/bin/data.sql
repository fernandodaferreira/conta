insert into conta(nome,dt_criacao,cnpj,cpf,nome_completo,dt_nascimento,id_conta_pai,tipo_conta,status,dt_status,saldo) values('Conta Fernando',now(),0,19531418896,'Fernando da Silva Ferreira','1975-10-23 00:00:00',0,'matriz','ativo',now(),100.00);
insert into conta(nome,dt_criacao,cnpj,cpf,razao_social,nome_fantasia,id_conta_pai,tipo_conta,status,dt_status,saldo) values('Conta Matriz FSF',now(),19531418896,0,'FSF Solutions','FESIFE',0,'matriz','ativo',now(),100.00);
insert into conta(nome,dt_criacao,cnpj,cpf,razao_social,nome_fantasia,id_conta_pai, tipo_conta,status,dt_status,saldo) values('Conta Filial FSF',now(),19531418897,0,'FSF Filial 1','FILIAL 1',2,'filial','ativo',now(),100.00);