create table if not exists universo (
    id bigserial primary key,
    nome varchar(60) not null unique,
    sigla varchar(8) not null unique,
    principais_series varchar(400),
    descricao varchar(800)
);

alter table gundam_kits
    add column if not exists universo_id bigint references universo(id),
    add column if not exists observacao varchar(2000);




