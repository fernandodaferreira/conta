create table conta
(
   id bigint auto_increment,
   nome varchar(100),
   dt_criacao timestamp,
   cnpj bigint,
   razao_social varchar(100),
   nome_fantasia varchar(100),
   cpf bigint,
   nome_completo varchar(100),
   dt_nascimento timestamp,
   id_conta_pai bigint,
   tipo_conta enum('matriz', 'filial'),
   status enum('ativo', 'bloqueada','cancelada'),
   dt_status timestamp,
   saldo double,
   primary key(id)
);


create table transacao
(
   id bigint auto_increment,
   tipo_transacao enum('aporte', 'transferencia'),
   dt_transacao timestamp,
   id_conta_debitada bigint,
   id_conta_creditada bigint,
   valor double,
   codigo_transacao uuid,
   primary key(id)
);
