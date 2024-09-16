CREATE
    EXTENSION IF NOT EXISTS postgis;
-- 기본 사용자 추가
INSERT INTO users (email, name, oauth_provider, oauth_id, profile_image_url, locale)
VALUES ('seojang0510@naver.com', '테스트사용자1', 'oauth_provider', 'oauth_id1', 'profile_image_url', 'ko-KR');

INSERT INTO users (email, name, oauth_provider, oauth_id, profile_image_url, locale)
VALUES ('wyscat@naver.com', '테스트사용자2', 'oauth_provider', 'oauth_id2', 'profile_image_url', 'ko-KR');

-- Parksp
INSERT INTO park (name, description, image_url)
VALUES ('싸피 뒷뜰', '싸피 구미 캠퍼스 기숙사에 위치한 공원으로 봄이 되면 예쁜 꽃들이 핀답니다. 가을이 되면 감도 딸 수 있어요',
        'https://example.com/seoul-forest.jpg'),
       ('동락공원', '아름다운 낙동강을 끼고 있는 넓은 산책로와 자전거 도로를 갖춘 도심 속 힐링 공간이에요. 산책로와 잔디밭이 있어서 마음껏 뛰어놀 수 있어요.
공원에는 농구장, 축구장, 배구장, 그리고 롤러스케이트장등 여러 가지 운동 시설도 많이 있어요. 시원한 바닥분수도 있어 시원한 여름을 보낼 수 있어요.
그리고 무료로 자전거를 대여할 수 있으니 자전거 타는 것도 잊지 마세요. 신나는 동락공원을 꼭 방문해보아요!',
        'https://example.com/olympic-park.jpg'),
       ('환경 연수원', '금오산 도립공원 구역 내에 위치하고 있는 수련 시설로 수생태체험학습장, 야외학습체험장 등등 다양한 학습 공간이 있어요.',
        'https://example.com/worldcup-park.jpg');
-- Park Position
INSERT INTO park_pos (pos, park_id, name)
VALUES (ST_SetSRID(ST_MakePoint(128.410590, 36.107442), 4326), 1, '정문'),  -- 싸피
       (ST_SetSRID(ST_MakePoint(128.417373, 36.107535), 4326), 1, '후문'),  -- 싸피
       (ST_SetSRID(ST_MakePoint(128.416504, 36.105738), 4326), 1, '개구멍'), -- 싸피
       (ST_SetSRID(ST_MakePoint(128.402023, 36.104893), 4326), 2, '북쪽 입구'),
       (ST_SetSRID(ST_MakePoint(128.401838, 36.101802), 4326), 2, '1 주차장'),
       (ST_SetSRID(ST_MakePoint(128.402133, 36.098217), 4326), 2, '중간 입구 1'),
       (ST_SetSRID(ST_MakePoint(128.402455, 36.096804), 4326), 2, '중간 입구 2'),
       (ST_SetSRID(ST_MakePoint(128.403072, 36.093575), 4326), 2, '4 주차장'),
       (ST_SetSRID(ST_MakePoint(128.403209, 36.092183), 4326), 2, '중간 입구'),
       (ST_SetSRID(ST_MakePoint(128.403233, 36.091906), 4326), 2, '5 주차장'),
       (ST_SetSRID(ST_MakePoint(128.402881, 36.089234), 4326), 2, '중간 입구'),
       (ST_SetSRID(ST_MakePoint(128.402859, 36.089024), 4326), 2, '6 주차장'),
       (ST_SetSRID(ST_MakePoint(128.400617, 36.085257), 4326), 2, '아래 입구'),
       (ST_SetSRID(ST_MakePoint(128.399493, 36.084151), 4326), 2, '7 주차장'),
       (ST_SetSRID(ST_MakePoint(128.398673, 36.083426), 4326), 2, '젤 아래 입구'),
       (ST_SetSRID(ST_MakePoint(128.311031, 36.119622), 4326), 3, '환경 연수원 입구');
-- 환경연수원
-- 환경 연수원
-- NPC
INSERT INTO npc (name)
VALUES ('수달'),      -- 1
       ('검사 수달'),   -- 2
       ('기도하는 수달'), -- 3
       ('너구리'),     -- 4
       ('마법사 너구리'), -- 5
       ('병아리'),     -- 6
       ('거북이'),     -- 7
       ('갈색 거북이'),  -- 8
       ('토끼'),      -- 9
       ('이정표'),     -- 10
       ('펭귄'),      -- 11
       ('돛새치');
-- 12
-- Park NPCs
INSERT INTO park_npc (park_id, npc_id)
VALUES (1, 1),  -- 1 싸피 뒷뜰, 수달
       (1, 2),  -- 2 싸피 뒷뜰, 검사 수달
       (1, 7),  -- 3 싸피 뒷뜰, 거북이
       (1, 10), -- 4 싸피 뒷뜰, 이정표
       (2, 3),  -- 5 동락공원, 기도하는 수달
       (2, 4),  -- 6 동락공원, 너구리
       (2, 8),  -- 7 동락공원, 갈색 거북이
       (2, 11), -- 8 동락공원. 펭귄
       (3, 5),  -- 9 환경연수원, 마법사 너구리
       (3, 6),  -- 10 환경연수원, 병아리
       (3, 9),  -- 11 환경연수원, 토끼
       (3, 12);
-- 12 환경연수원, 돛새치
-- 환경 연수원
-- NPC Position
INSERT INTO npc_pos (pos, park_npc_id)
VALUES (ST_SetSRID(ST_MakePoint(128.416000, 36.107000), 4326), 1), -- 싸피 뒷뜰 - 수달
       (ST_SetSRID(ST_MakePoint(128.416100, 36.107100), 4326), 1), -- 싸피 뒷뜰 - 수달
       (ST_SetSRID(ST_MakePoint(128.416200, 36.107200), 4326), 2), -- 싸피 뒷뜰 - 검사 수달
       (ST_SetSRID(ST_MakePoint(128.416300, 36.107300), 4326), 2), -- 싸피 뒷뜰 - 검사 수달
       (ST_SetSRID(ST_MakePoint(128.416400, 36.107400), 4326), 3), -- 싸피 뒷뜰 - 거북이
       (ST_SetSRID(ST_MakePoint(128.416500, 36.107500), 4326), 4), -- 싸피 뒷뜰 - 이정표
       (ST_SetSRID(ST_MakePoint(128.402000, 36.100000), 4326), 5), -- 동락공원 - 기도하는 수달
       (ST_SetSRID(ST_MakePoint(128.402500, 36.095000), 4326), 6), -- 동락공원 - 너구리
       (ST_SetSRID(ST_MakePoint(128.311500, 36.119000), 4326), 9);
-- 환경 연수원 - 마법사 너구리
-- Species
INSERT INTO species (name, scientific_name, description, image_url)
VALUES ('청설모', 'Sciurus vulgaris', '귀여운 다람쥐과의 포유류', 'https://example.com/squirrel.jpg'),
       ('왕벚나무', 'Prunus yedoensis', '봄에 아름다운 꽃을 피우는 나무', 'https://example.com/cherry-tree.jpg'),
       ('잉어', 'Cyprinus carpio', '연못에서 흔히 볼 수 있는 물고기', 'https://example.com/carp.jpg'),
       ('참새', 'Passer montanus', '도시에서 흔히 볼 수 있는 작은 새', 'https://example.com/sparrow.jpg'),
       ('단풍나무', 'Acer palmatum', '가을에 아름다운 단풍을 보여주는 나무', 'https://example.com/maple.jpg'),
       ('개구리', 'Rana coreana', '한국에서 흔히 볼 수 있는 개구리', 'https://example.com/frog.jpg');
-- Park Species
INSERT INTO park_species (park_id, species_id)
VALUES (1, 1), -- 1 싸피 뒷뜰 - 청설모
       (1, 2), -- 2 싸피 뒷뜰 - 왕벚나무
       (1, 4), -- 3 싸피 뒷뜰 - 참새
       (2, 2), -- 4 동락공원 - 왕벚나무
       (2, 3), -- 5 동락공원 - 잉어
       (2, 5), -- 6 동락공원 - 단풍나무
       (3, 1), -- 7 환경 연수원 - 청설모
       (3, 5), -- 8 환경 연수원 - 단풍나무
       (3, 6)
-- 9 환경 연수원 - 개구리
ON CONFLICT (park_id, species_id) DO NOTHING;
-- Species Position
INSERT INTO species_pos (pos, park_species_id)
VALUES
    -- 싸피 뒷뜰 - 청설모
    (ST_SetSRID(ST_MakePoint(128.416000, 36.107000), 4326), 1),
    (ST_SetSRID(ST_MakePoint(128.416100, 36.107100), 4326), 1),
    (ST_SetSRID(ST_MakePoint(128.416200, 36.107200), 4326), 1),
    -- 싸피 뒷뜰 - 왕벚나무
    (ST_SetSRID(ST_MakePoint(128.416200, 36.107200), 4326), 2),
    (ST_SetSRID(ST_MakePoint(128.416300, 36.107300), 4326), 2),
    -- 싸피 뒷뜰 - 참새
    (ST_SetSRID(ST_MakePoint(128.416400, 36.107400), 4326), 3),
    (ST_SetSRID(ST_MakePoint(128.416500, 36.107500), 4326), 3),
    (ST_SetSRID(ST_MakePoint(128.416600, 36.107600), 4326), 3),
    -- 동락공원 - 왕벚나무
    (ST_SetSRID(ST_MakePoint(128.402000, 36.100000), 4326), 4),
    (ST_SetSRID(ST_MakePoint(128.402100, 36.100100), 4326), 4),
    -- 동락공원 - 잉어
    (ST_SetSRID(ST_MakePoint(128.402500, 36.095000), 4326), 5),
    (ST_SetSRID(ST_MakePoint(128.402600, 36.095100), 4326), 5),
    (ST_SetSRID(ST_MakePoint(128.402700, 36.095200), 4326), 5),
    -- 동락공원 - 단풍나무
    (ST_SetSRID(ST_MakePoint(128.403000, 36.090000), 4326), 6),
    (ST_SetSRID(ST_MakePoint(128.403100, 36.090100), 4326), 6),
    -- 환경 연수원 - 청설모
    (ST_SetSRID(ST_MakePoint(128.311500, 36.119000), 4326), 7),
    (ST_SetSRID(ST_MakePoint(128.311600, 36.119100), 4326), 7),
    -- 환경 연수원 - 단풍나무
    (ST_SetSRID(ST_MakePoint(128.311700, 36.119500), 4326), 8),
    (ST_SetSRID(ST_MakePoint(128.311800, 36.119600), 4326), 8),
    (ST_SetSRID(ST_MakePoint(128.311900, 36.119700), 4326), 8),
    -- 환경 연수원 - 개구리
    (ST_SetSRID(ST_MakePoint(128.312000, 36.120000), 4326), 9),
    (ST_SetSRID(ST_MakePoint(128.312100, 36.120100), 4326), 9);
-- Quest
INSERT INTO quest (type)
VALUES (1),
       (2),
       (3);

-- Discovery 데이터 삽입
INSERT INTO discovery (user_id, species_id, species_pos_id, discovery_time, image_url)
SELECT 1, s.species_id, sp.id, NOW(), 'https://example.com/discovered_squirrel.jpg'
FROM species s
         JOIN park_species ps ON s.species_id = ps.species_id
         JOIN species_pos sp ON ps.id = sp.park_species_id
WHERE s.name = '청설모' AND ST_Equals(sp.pos, ST_SetSRID(ST_MakePoint(128.416000, 36.107000), 4326));