drop table if exists tb_lancamento;
drop table if exists tb_funcionario;
drop table if exists tb_empresa;


CREATE TABLE public.tb_empresa (
	id bigserial NOT NULL,
	cnpj TEXT NOT NULL,
	data_atualizacao TIMESTAMP NOT NULL,
	data_criacao TIMESTAMP NOT NULL,
	razao_social TEXT NOT NULL,
	CONSTRAINT tb_empresa_pkey PRIMARY KEY (id)
);
-- Permissions
--ALTER TABLE public.tb_empresa OWNER TO sa;
--GRANT ALL ON TABLE public.tb_empresa TO sa;
   
CREATE TABLE public.tb_funcionario (
	id bigserial NOT NULL,
	cpf TEXT NOT NULL,
	data_atualizacao TIMESTAMP NOT NULL,
	data_criacao TIMESTAMP NOT NULL,
	email TEXT NOT NULL,
	nome TEXT NOT NULL,
	perfil TEXT NOT NULL,
	qtd_horas_almoco FLOAT8 NULL,
	qtd_horas_trabalho_dia FLOAT8 NULL,
	senha TEXT NOT NULL,
	valor_hora numeric(19,2) NULL,
	empresa_id int8 NOT NULL,
	CONSTRAINT tb_funcionario_pkey PRIMARY KEY (id),
	CONSTRAINT tb_funcionario_tb_empresa_fk FOREIGN KEY (empresa_id) REFERENCES tb_empresa(id)
);
-- Permissions
--ALTER TABLE public.tb_funcionario OWNER TO sa;
--GRANT ALL ON TABLE public.tb_funcionario TO sa;
    

CREATE TABLE public.tb_lancamento (
	id bigserial NOT NULL,
	data TIMESTAMP NOT NULL,
	data_atualizacao TIMESTAMP NOT NULL,
	data_criacao TIMESTAMP NOT NULL,
	descricao TEXT,
	localizacao TEXT,
	tipo TEXT NOT NULL,
	funcionario_id int8 NOT NULL,
	CONSTRAINT tb_lancamento_pkey PRIMARY KEY (id),
	CONSTRAINT tb_lancamento_tb_funcionario_fk FOREIGN KEY (funcionario_id) REFERENCES tb_funcionario(id)
);
-- Permissions
--ALTER TABLE public.tb_lancamento OWNER TO sa;
--GRANT ALL ON TABLE public.tb_lancamento TO sa;