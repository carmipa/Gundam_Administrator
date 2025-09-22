-- Grades (com base na sua planilha)
insert into grade (nome) values
                             ('SD (Super Deformed)'),
                             ('EG (Entry Grade)'),
                             ('HG (High Grade)'),
                             ('RG (Real Grade)'),
                             ('RE/100 (Full Mechanics)'),
                             ('MG (Master Grade)'),
                             ('Hi-RM (Hi-Resolution Model)'),
                             ('PG (Perfect Grade)'),
                             ('Mega Size Model');

-- Escalas
insert into escala (rotulo) values
                                ('Sem escala'),
                                ('1/144'),
                                ('1/100'),
                                ('1/60'),
                                ('1/48');

-- Alturas padrão (texto direto, mas você pode normalizar em cm depois)
insert into altura_padrao (descricao) values
                                          ('~ 8 - 10 cm'),
                                          ('~ 13 cm'),
                                          ('12 - 15 cm'),
                                          ('~ 13 cm (RG)'),
                                          ('18 - 22 cm'),
                                          ('30 - 35 cm'),
                                          ('~ 37.5 cm');

-- Alguns mapeamentos típicos (ex.: HG 1/144 12-15cm)
-- Você seleciona no formulário; a "altura" é escolhida do catálogo acima.
