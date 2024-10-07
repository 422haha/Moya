CREATE
EXTENSION IF NOT EXISTS postgis;
-- 기본 사용자 추가
INSERT INTO users (email, name, oauth_provider, oauth_id, profile_image_url)
VALUES ('seojang0510@naver.com', '테스트사용자1', 'oauth_provider', 'oauth_id1', 'profile_image_url');

INSERT INTO users (email, name, oauth_provider, oauth_id, profile_image_url)
VALUES ('wyscat@naver.com', '테스트사용자2', 'oauth_provider', 'oauth_id2', 'profile_image_url');

-- Parksp
INSERT INTO park (name, description, image_url)
VALUES ('싸피 뒷뜰', '싸피 구미 캠퍼스 기숙사에 위치한 공원으로 봄이 되면 예쁜 꽃들이 핀답니다. 가을이 되면 감도 딸 수 있어요',
        'ssafy.jpg'),
       ('동락공원', '아름다운 낙동강을 끼고 있는 넓은 산책로와 자전거 도로를 갖춘 도심 속 힐링 공간이에요. 산책로와 잔디밭이 있어서 마음껏 뛰어놀 수 있어요.
공원에는 농구장, 축구장, 배구장, 그리고 롤러스케이트장등 여러 가지 운동 시설도 많이 있어요. 시원한 바닥분수도 있어 시원한 여름을 보낼 수 있어요.
그리고 무료로 자전거를 대여할 수 있으니 자전거 타는 것도 잊지 마세요. 신나는 동락공원을 꼭 방문해보아요!',
        'dongrac.jpg'),
       ('환경 연수원', '금오산 도립공원 구역 내에 위치하고 있는 수련 시설로 수생태체험학습장, 야외학습체험장 등등 다양한 학습 공간이 있어요.',
        'yonsuwon.jpg'),
       ('지산샛강생태공원',
        '구미의 유일한 습지인 지산샛강생태공원은 봄엔 아름다운 벚꽃산책길, 여름엔 연꽃 군락지, 겨울엔 철새 보금자리로 사계절 즐길거리가 많으며, 도심에서 가까운 곳에 위치해 있어 접근성이 좋아요. 천연기념물 큰고니의 최대 서식지로 잘 알려져있어요. 큰고니를 보고싶으면 방문해보세요.',
        'jisan-setgang.jpg');
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
       (ST_SetSRID(ST_MakePoint(128.311031, 36.119622), 4326), 3, '환경 연수원 입구'),
       (ST_SetSRID(ST_MakePoint(128.352066, 36.137240), 4326), 4, '지산샛강 생태공원 입구');
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
VALUES (ST_SetSRID(ST_MakePoint(128.4162641, 36.1067967), 4326), 1), -- 싸피 뒷뜰 - 수달
       (ST_SetSRID(ST_MakePoint(128.4161918, 36.1066985), 4326), 2), -- 싸피 뒷뜰 - 수달
       (ST_SetSRID(ST_MakePoint(128.4163712, 36.1066493), 4326), 3), -- 싸피 뒷뜰 - 검사 수달
       (ST_SetSRID(ST_MakePoint(128.4165288, 36.1071543), 4326), 3), -- 싸피 뒷뜰 - 검사 수달
       (ST_SetSRID(ST_MakePoint(128.4199104, 36.101726), 4326), 4),  -- 싸피 뒷뜰 - 검사 수달
       (ST_SetSRID(ST_MakePoint(128.402000, 36.100000), 4326), 5),   -- 동락공원 - 기도하는 수달
       (ST_SetSRID(ST_MakePoint(128.402500, 36.095000), 4326), 6),   -- 동락공원 - 너구리
       (ST_SetSRID(ST_MakePoint(128.311500, 36.119000), 4326), 9);
-- 환경 연수원 - 마법사 너구리
-- Species
INSERT INTO species (name, scientific_name, description, image_url)
VALUES ('강아지풀', 'Setaria viridis', '강아지 꼬리처럼 복슬복슬 귀여운 강아지풀', 'foxtail.jpg'),
       ('단풍잎', 'Acer palmatum', '가을이 되면 물드는 단풍잎', 'maple-leaves.jpg'),
       ('솔방울', 'Pinus densiflora Siebold & Zucc', '씨앗이 들어있는 자그마한 비늘들이 둥글게 모인 소나무 열매의 송이',
        'pine-cones.jpg'),
       ('란타나', 'Lantana camara', '다양한 색깔의 꽃을 피우는 관목', 'lantana.jpg'),
       ('히비스커스', 'Hibiscus rosa-sinensis', '큰 꽃을 피우는 열대성 식물', 'hibiscus.jpg'),
       ('야트로파 쿠르카스', 'Jatropha curcas', '고소한 향을 가진 꽃을 피우는 나무', 'jatropha-curcas.jpg'),
       ('메리골드', 'Tagetes erecta', '주황색 꽃을 피우는 한해살이 식물', 'marigold.jpg'),
       ('장미', 'Rosa', '전 세계적으로 사랑받는 꽃으로 다양한 품종이 존재', 'rose.jpg'),
       ('참파카초령목', 'Magnolia champaca', '향기로운 꽃을 피우는 나무', 'champaca.jpg'),
       ('백화단', 'Plumbago zeylanica', '작은 흰색 꽃을 피우는 관목', 'plumbago-zeylanica.jpg'),
       ('인동덩굴', 'Lonicera', '달콤한 향을 가진 덩굴식물', 'lonicera.jpg'),
       ('아부틸론 인디쿰', 'Abutilon indicum', '작은 노란 꽃을 피우는 식물', 'abutilon-indicum.jpg'),
       ('야모란과', 'Melastoma malabathricum', '보라색 꽃을 피우는 관목', 'melastoma-malabathricum.jpg'),
       ('버터플라이피', 'Clitoria ternatea', '파란색 꽃을 피우는 덩굴식물', 'clitoria-ternatea.jpg'),
       ('히메노칼리스', 'Hymenocallis littoralis', '거미 모양의 꽃을 피우는 식물', 'hymenocallis-littoralis.jpg'),
       ('해바라기', 'Helianthus annuus', '커다란 노란 꽃을 피우는 식물', 'sunflowers.jpg');

-- 동식물의 볼 수 있는 계절 정보 삽입
INSERT INTO species_seasons (species_id, season)
VALUES (1, 'SPRING'),  -- 강아지풀: 봄, 여름
       (1, 'SUMMER'),  -- 강아지풀: 봄, 여름
       (2, 'AUTUMN'),  -- 단풍잎: 가을
       (3, 'SPRING'),  -- 솔방울: 봄, 여름, 가을
       (3, 'SUMMER'),  -- 솔방울: 봄, 여름, 가을
       (3, 'AUTUMN'),  -- 솔방울: 봄, 여름, 가을
       (4, 'SPRING'),  -- 란타나: 봄, 여름
       (4, 'SUMMER'),
       (4, 'AUTUMN'),  -- 란타나: 가을
       (5, 'SPRING'),  -- 히비스커스: 봄, 여름, 가을
       (5, 'SUMMER'),
       (5, 'AUTUMN'),
       (6, 'SPRING'),  -- 야트로파 쿠르카스: 봄, 여름
       (6, 'SUMMER'),
       (7, 'SPRING'),  -- 메리골드: 봄, 여름, 가을
       (7, 'SUMMER'),
       (7, 'AUTUMN'),
       (8, 'SPRING'),  -- 장미: 봄, 여름, 가을, 겨울 (모든 계절)
       (8, 'SUMMER'),
       (8, 'AUTUMN'),
       (8, 'WINTER'),
       (9, 'SPRING'),  -- 참파카초령목: 봄, 여름
       (9, 'SUMMER'),
       (10, 'SPRING'), -- 백화단: 봄, 여름
       (10, 'SUMMER'),
       (11, 'SPRING'), -- 인동덩굴: 봄
       (12, 'SPRING'), -- 아부틸론 인디쿰: 봄, 여름
       (12, 'SUMMER'),
       (13, 'SPRING'), -- 야모란과: 봄, 여름, 가을
       (13, 'SUMMER'),
       (13, 'AUTUMN'),
       (14, 'SUMMER'), -- 버터플라이피: 여름, 가을
       (14, 'AUTUMN'),
       (15, 'SUMMER'), -- 히메노칼리스: 여름, 가을
       (15, 'AUTUMN'),
       (16, 'SUMMER'), -- 해바라기: 여름, 가을
       (16, 'AUTUMN');

-- park_species 테이블에 유일 제약 조건 추가
-- ALTER TABLE park_species
--     ADD CONSTRAINT unique_park_species UNIQUE (park_id, species_id);

-- Park Species
INSERT INTO park_species (park_id, species_id)
VALUES (1, 1),  -- 1 싸피 뒷뜰 - 강아지풀
       (1, 2),  -- 2 싸피 뒷뜰 - 단풍잎
       (1, 3),  -- 3 싸피 뒷뜰 - 솔방울
       (2, 1),  -- 4 동락공원 - 강아지풀
       (2, 2),  -- 5 동락공원 - 단풍잎
       (2, 3),  -- 6 동락공원 - 솔방울
       (2, 8),  -- 7 동락공원 - 장미
       (2, 16), -- 8 동락공원 - 해바라기
       (3, 1),  -- 9 환경 연수원 - 강아지풀
       (3, 2),  -- 10 환경 연수원 - 단풍잎
       (3, 3),  -- 11 환경 연수원 - 솔방울
       (3, 8),  -- 12 환경 연수원 - 장미
       (3, 16)  -- 13 환경 연수원 - 해바라기
ON CONFLICT
    (park_id, species_id)
DO NOTHING;

-- Species Position
INSERT INTO species_pos (pos, park_species_id)
VALUES
    -- 싸피 뒷뜰 - 강아지풀
    (ST_SetSRID(ST_MakePoint(128.416200, 36.107200), 4326), 1),
    (ST_SetSRID(ST_MakePoint(128.416300, 36.107300), 4326), 1),
    (ST_SetSRID(ST_MakePoint(128.416400, 36.107400), 4326), 1),
    -- 싸피 뒷뜰 - 단풍잎
    (ST_SetSRID(ST_MakePoint(128.416000, 36.107000), 4326), 2),
    (ST_SetSRID(ST_MakePoint(128.416100, 36.107100), 4326), 2),
    (ST_SetSRID(ST_MakePoint(128.416200, 36.107200), 4326), 2),
    -- 싸피 뒷뜰 - 솔방울
    (ST_SetSRID(ST_MakePoint(128.416500, 36.107500), 4326), 3),
    (ST_SetSRID(ST_MakePoint(128.416600, 36.107600), 4326), 3),
    -- 동락공원 - 강아지풀
    (ST_SetSRID(ST_MakePoint(128.402500, 36.095000), 4326), 4),
    (ST_SetSRID(ST_MakePoint(128.402600, 36.095100), 4326), 4),
    -- 동락공원 - 단풍잎
    (ST_SetSRID(ST_MakePoint(128.402000, 36.100000), 4326), 5),
    (ST_SetSRID(ST_MakePoint(128.402100, 36.100100), 4326), 5),
    -- 동락공원 - 솔방울
    (ST_SetSRID(ST_MakePoint(128.402700, 36.095200), 4326), 6),
    -- 동락공원 - 장미
    (ST_SetSRID(ST_MakePoint(128.403000, 36.090000), 4326), 7),
    -- 동락공원 - 해바라기
    (ST_SetSRID(ST_MakePoint(128.403100, 36.090100), 4326), 8),
    -- 환경 연수원 - 강아지풀
    (ST_SetSRID(ST_MakePoint(128.311700, 36.119500), 4326), 9),
    -- 환경 연수원 - 단풍잎
    (ST_SetSRID(ST_MakePoint(128.311500, 36.119000), 4326), 10),
    (ST_SetSRID(ST_MakePoint(128.311600, 36.119100), 4326), 10),
    -- 환경 연수원 - 솔방울
    (ST_SetSRID(ST_MakePoint(128.311800, 36.119600), 4326), 11),
    (ST_SetSRID(ST_MakePoint(128.311900, 36.119700), 4326), 11),
    -- 환경 연수원 - 장미
    (ST_SetSRID(ST_MakePoint(128.312000, 36.120000), 4326), 12),
    -- 환경 연수원 - 해바라기
    (ST_SetSRID(ST_MakePoint(128.312100, 36.120100), 4326), 13);

-- ==============================================test
-- 1. 멀찍히 떨어진 개별 점 (강아지풀)
INSERT INTO species_pos (pos, park_species_id)
VALUES (ST_SetSRID(ST_MakePoint(128.410100, 36.107100), 4326), 1),
       (ST_SetSRID(ST_MakePoint(128.415100, 36.107100), 4326), 1),
       (ST_SetSRID(ST_MakePoint(128.412100, 36.109100), 4326), 1);
-- 1. 멀찍히 떨어진 개별 점 (단풍잎)
INSERT INTO species_pos (pos, park_species_id)
VALUES (ST_SetSRID(ST_MakePoint(128.410000, 36.107000), 4326), 2),
       (ST_SetSRID(ST_MakePoint(128.415000, 36.107000), 4326), 2),
       (ST_SetSRID(ST_MakePoint(128.412000, 36.109000), 4326), 2);

-- 2. 반경 20m 안에 3점이 존재하는 경우 (강아지풀)
INSERT INTO species_pos (pos, park_species_id)
VALUES (ST_SetSRID(ST_MakePoint(128.413000, 36.108000), 4326), 1),
       (ST_SetSRID(ST_MakePoint(128.413010, 36.108010), 4326), 1),
       (ST_SetSRID(ST_MakePoint(128.413020, 36.108020), 4326), 1);
-- 2. 반경 20m 안에 3점이 존재하는 경우 (솔방울)
INSERT INTO species_pos (pos, park_species_id)
VALUES (ST_SetSRID(ST_MakePoint(128.413000, 36.108000), 4326), 3),
       (ST_SetSRID(ST_MakePoint(128.413010, 36.108010), 4326), 3),
       (ST_SetSRID(ST_MakePoint(128.413020, 36.108020), 4326), 3);

-- 3. 반경 100m 안에 2번 조건을 만족하는 점이 무수히 많은 경우 (솔방울)
INSERT INTO species_pos (pos, park_species_id)
SELECT ST_SetSRID(ST_MakePoint(
                          128.414000 + (random() * 0.001),
                          36.106000 + (random() * 0.001)
                  ), 4326),
       3
FROM generate_series(1, 50);
-- ==============================================test

-- Quest
INSERT INTO quest (type)
VALUES (1),
       (2),
       (3);

-- -- Discovery 데이터 삽입
-- INSERT INTO discovery (user_id, species_id, species_pos_id, discovery_time, image_url)
-- SELECT 1, s.species_id, sp.id, NOW(), 'https://example.com/discovered_squirrel.jpg'
-- FROM species s
--          JOIN park_species ps ON s.species_id = ps.species_id
--          JOIN species_pos sp ON ps.id = sp.park_species_id
-- WHERE s.name = '단풍잎'
--   AND ST_Equals(sp.pos, ST_SetSRID(ST_MakePoint(128.416000, 36.107000), 4326));

-- Exploration Data Insertion (탐험 데이터 삽입)
INSERT INTO exploration (user_id, park_id, start_time, end_time, distance, steps, startdate,
                         image_url, route, completed)
VALUES (1, 1, '2024-09-23 10:00:00', '2024-09-23 12:00:00', 2500, 5000, '2024-09-23',
        'test-camera.png',
        ST_GeomFromText(
                'LINESTRING(128.416000 36.107000, 128.416100 36.107100, 128.416200 36.107200)',
                4326), true),
       (1, 2, '2024-09-24 09:00:00', '2024-09-24 11:30:00', 5000, 8000, '2024-09-24',
        'test-camera.png',
        ST_GeomFromText(
                'LINESTRING(128.402000 36.100000, 128.402100 36.100100, 128.402500 36.095000)',
                4326), true),
       (1, 3, '2024-09-25 14:00:00', '2024-09-25 16:00:00', 3000, 6000, '2024-09-25',
        'test-camera.png',
        ST_GeomFromText(
                'LINESTRING(128.311500 36.119000, 128.311600 36.119100, 128.312000 36.120000)',
                4326), true);

-- Discovery Data Insertion (발견 데이터 삽입)
-- 강아지풀 발견 데이터
INSERT INTO discovery (user_id, species_id, species_pos_id, discovery_time, image_url)
SELECT 1,                     -- user_id (테스트사용자1)
       1,                     -- species_id ('강아지풀')
       sp.id,                 -- species_pos_id
       '2024-09-23 11:00:00', -- discovery_time
       'test-camera.png'      -- image_url (강아지풀 이미지)
FROM species_pos sp
         JOIN park_species ps ON ps.id = sp.park_species_id
WHERE ps.park_id = 1    -- '싸피 뒷뜰'
  AND ps.species_id = 1 -- 강아지풀
  AND ST_Equals(sp.pos, ST_SetSRID(ST_MakePoint(128.416200, 36.107200), 4326));

-- 단풍잎 발견 데이터
INSERT INTO discovery (user_id, species_id, species_pos_id, discovery_time, image_url)
SELECT 1,                     -- user_id (테스트사용자1)
       2,                     -- species_id ('단풍잎')
       sp.id,                 -- species_pos_id
       '2024-09-23 10:30:00', -- discovery_time
       'test-camera.png'      -- image_url (단풍잎 이미지)
FROM species_pos sp
         JOIN park_species ps ON ps.id = sp.park_species_id
WHERE ps.park_id = 1    -- '싸피 뒷뜰'
  AND ps.species_id = 2 -- 단풍잎
  AND ST_Equals(sp.pos, ST_SetSRID(ST_MakePoint(128.416000, 36.107000), 4326));

-- 솔방울 발견 데이터
INSERT INTO discovery (user_id, species_id, species_pos_id, discovery_time, image_url)
SELECT 1,                     -- user_id (테스트사용자2)
       3,                     -- species_id ('솔방울')
       sp.id,                 -- species_pos_id
       '2024-09-24 10:15:00', -- discovery_time
       'test-camera.png'      -- image_url (솔방울 이미지)
FROM species_pos sp
         JOIN park_species ps ON ps.id = sp.park_species_id
WHERE ps.park_id = 2    -- 동락공원
  AND ps.species_id = 3 -- 솔방울
  AND ST_Equals(sp.pos, ST_SetSRID(ST_MakePoint(128.402500, 36.095000), 4326));

-- 장미 발견 데이터
INSERT INTO discovery (user_id, species_id, species_pos_id, discovery_time, image_url)
SELECT 1,                     -- user_id (테스트사용자2)
       5,                     -- species_id ('단풍나무')
       sp.id,                 -- species_pos_id
       '2024-09-24 11:00:00', -- discovery_time
       'test-camera.png'      -- image_url (단풍나무 이미지)
FROM species_pos sp
         JOIN park_species ps ON ps.id = sp.park_species_id
WHERE ps.park_id = 2    -- 동락공원
  AND ps.species_id = 8 -- 장미
  AND ST_Equals(sp.pos, ST_SetSRID(ST_MakePoint(128.403000, 36.090000), 4326));

-- 해바라기 발견 데이터
INSERT INTO discovery (user_id, species_id, species_pos_id, discovery_time, image_url)
SELECT 1,                     -- user_id (테스트사용자1)
       6,                     -- species_id ('개구리')
       sp.id,                 -- species_pos_id
       '2024-09-25 15:00:00', -- discovery_time
       'test-camera.png'      -- image_url (개구리 이미지)
FROM species_pos sp
         JOIN park_species ps ON ps.id = sp.park_species_id
WHERE ps.park_id = 3     -- 환경 연수원
  AND ps.species_id = 16 -- 해바라기
  AND ST_Equals(sp.pos, ST_SetSRID(ST_MakePoint(128.312000, 36.120000), 4326));



-- ========================================================공원 더미 데이터
-- 테스트용 공원 20개 추가
INSERT INTO park (name, description, image_url)
VALUES ('Test Park 1', '테스트 공원 1 설명', 'park1.jpg'),
       ('Test Park 2', '테스트 공원 2 설명', 'park2.jpg'),
       ('Test Park 3', '테스트 공원 3 설명', 'park3.jpg'),
       ('Test Park 4', '테스트 공원 4 설명', 'park4.jpg'),
       ('Test Park 5', '테스트 공원 5 설명', 'park5.jpg'),
       ('Test Park 6', '테스트 공원 6 설명', 'park6.jpg'),
       ('Test Park 7', '테스트 공원 7 설명', 'park7.jpg'),
       ('Test Park 8', '테스트 공원 8 설명', 'park8.jpg'),
       ('Test Park 9', '테스트 공원 9 설명', 'park9.jpg'),
       ('Test Park 10', '테스트 공원 10 설명', 'park10.jpg'),
       ('Test Park 11', '테스트 공원 11 설명', 'park11.jpg'),
       ('Test Park 12', '테스트 공원 12 설명', 'park12.jpg'),
       ('Test Park 13', '테스트 공원 13 설명', 'park13.jpg'),
       ('Test Park 14', '테스트 공원 14 설명', 'park13.jpg'),
       ('Test Park 15', '테스트 공원 15 설명', 'park13.jpg'),
       ('Test Park 16', '테스트 공원 16 설명', 'park13.jpg'),
       ('Test Park 17', '테스트 공원 17 설명', 'park13.jpg'),
       ('Test Park 18', '테스트 공원 18 설명', 'park13.jpg'),
       ('Test Park 19', '테스트 공원 19 설명', 'park13.jpg'),
       ('Test Park 20', '테스트 공원 20 설명', 'park13.jpg');

-- 테스트용 공원 위치 추가
INSERT INTO park_pos (pos, park_id, name)
SELECT ST_SetSRID(
               ST_MakePoint(
                       128.416431 + (random() - 0.5) * 0.7,
                       36.108044 + (random() - 0.5) * 0.7
               ),
               4326
       ),
       p.id,
       '입구'
FROM park p
WHERE p.name LIKE 'Test Park %'
ORDER BY p.id;
-- ========================================================공원 더미 데이터