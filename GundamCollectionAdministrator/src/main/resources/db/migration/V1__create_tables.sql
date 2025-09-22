create table grade (
                       id bigserial primary key,
                       nome varchar(50) not null unique
);

create table escala (
                        id bigserial primary key,
                        rotulo varchar(20) not null unique
);

create table altura_padrao (
                               id bigserial primary key,
                               descricao varchar(30) not null unique
);

-- Tabela principal alinhada às entidades (@Table(name = 'gundam_kits'))
create table gundam_kits (
                              id bigserial primary key,
                              modelo varchar(200) not null,
                              fabricante varchar(100) not null default 'Bandai',
                              preco numeric(12,2),
                              data_compra date,

                              capa_url varchar(500),
                              foto_caixa_url varchar(500),
                              foto_montagem_url varchar(500),
                              video_montagem_url varchar(500),

                              horas_montagem integer,

                              grade_id bigint references grade(id),
                              escala_id bigint references escala(id),
                              altura_padrao_id bigint references altura_padrao(id),

                              created_at timestamp with time zone default now()
);

create index idx_gundam_kits_modelo on gundam_kits(modelo);
