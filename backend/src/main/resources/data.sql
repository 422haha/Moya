CREATE
EXTENSION IF NOT EXISTS postgis;
-- 기본 사용자 추가
INSERT INTO users (email, name, oauth_provider, oauth_id, profile_image_url, locale)
VALUES ('seojang0510@naver.com', '테스트사용자1', 'oauth_provider', 'oauth_id1', 'profile_image_url',
        'ko-KR');

INSERT INTO users (email, name, oauth_provider, oauth_id, profile_image_url, locale)
VALUES ('wyscat@naver.com', '테스트사용자2', 'oauth_provider', 'oauth_id2', 'profile_image_url', 'ko-KR');

-- Parksp
INSERT INTO park (name, description, image_url)
VALUES ('싸피 뒷뜰', '싸피 구미 캠퍼스 기숙사에 위치한 공원으로 봄이 되면 예쁜 꽃들이 핀답니다. 가을이 되면 감도 딸 수 있어요',
        'https://i.ibb.co/4jww8VF/1.jpg'),
       ('동락공원', '아름다운 낙동강을 끼고 있는 넓은 산책로와 자전거 도로를 갖춘 도심 속 힐링 공간이에요. 산책로와 잔디밭이 있어서 마음껏 뛰어놀 수 있어요.
공원에는 농구장, 축구장, 배구장, 그리고 롤러스케이트장등 여러 가지 운동 시설도 많이 있어요. 시원한 바닥분수도 있어 시원한 여름을 보낼 수 있어요.
그리고 무료로 자전거를 대여할 수 있으니 자전거 타는 것도 잊지 마세요. 신나는 동락공원을 꼭 방문해보아요!',
        'https://i.ibb.co/m5LSDV6/2.jpg'),
       ('환경 연수원', '금오산 도립공원 구역 내에 위치하고 있는 수련 시설로 수생태체험학습장, 야외학습체험장 등등 다양한 학습 공간이 있어요.',
        'https://i.ibb.co/4TBJ2Bh/3.jpg');
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
       ('갈색 거북이'), -- 8
       ('토끼'),       -- 9
       ('이정표'),     -- 10
       ('펭귄'),       -- 11
       ('돛새치');     -- 12
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
VALUES ('청설모', 'Sciurus vulgaris', '귀여운 다람쥐과의 포유류', 'https://i.ibb.co/HnfJcXB/image.jpg'),
       ('왕벚나무', 'Prunus yedoensis', '봄에 아름다운 꽃을 피우는 나무', 'https://i.ibb.co/ftzKQ97/image.jpg'),
       ('잉어', 'Cyprinus carpio', '연못에서 흔히 볼 수 있는 물고기', 'https://i.ibb.co/brQGP4d/image.jpg'),
       ('참새', 'Passer montanus', '도시에서 흔히 볼 수 있는 작은 새', 'https://i.ibb.co/my2KB2k/image.jpg'),
       ('단풍나무', 'Acer palmatum', '가을에 아름다운 단풍을 보여주는 나무', 'https://i.ibb.co/V2wXDVt/image.jpg'),
       ('개구리', 'Rana coreana', '한국에서 흔히 볼 수 있는 개구리', 'https://i.ibb.co/hfRc519/image.jpg');

-- 동식물의 볼 수 있는 계절 정보 삽입
INSERT INTO species_seasons (species_id, season)
VALUES (1, 'SPRING'),
       (1, 'SUMMER'),
       (1, 'AUTUMN'),
       (1, 'WINTER'), -- 청설모: 모든 계절
       (2, 'SPRING'), -- 왕벚나무: 봄
       (3, 'SPRING'),
       (3, 'SUMMER'),
       (3, 'AUTUMN'), -- 잉어: 봄, 여름, 가을
       (4, 'SPRING'),
       (4, 'SUMMER'),
       (4, 'AUTUMN'),
       (4, 'WINTER'), -- 참새: 모든 계절
       (5, 'AUTUMN'), -- 단풍나무: 가을
       (6, 'SPRING'),
       (6, 'SUMMER');
-- 개구리: 봄, 여름

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
ON CONFLICT
    (park_id, species_id)
DO NOTHING;
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

-- ==============================================test
-- 1. 멀찍히 떨어진 개별 점 (청설모)
INSERT INTO species_pos (pos, park_species_id)
VALUES (ST_SetSRID(ST_MakePoint(128.410000, 36.107000), 4326), 1),
       (ST_SetSRID(ST_MakePoint(128.415000, 36.107000), 4326), 1),
       (ST_SetSRID(ST_MakePoint(128.412000, 36.109000), 4326), 1);
-- 1. 멀찍히 떨어진 개별 점 (왕벛나무)
INSERT INTO species_pos (pos, park_species_id)
VALUES (ST_SetSRID(ST_MakePoint(128.410100, 36.107100), 4326), 2),
       (ST_SetSRID(ST_MakePoint(128.415100, 36.107100), 4326), 2),
       (ST_SetSRID(ST_MakePoint(128.412100, 36.109100), 4326), 2);

-- 2. 반경 20m 안에 3점이 존재하는 경우 (왕벚나무)
INSERT INTO species_pos (pos, park_species_id)
VALUES (ST_SetSRID(ST_MakePoint(128.413000, 36.108000), 4326), 2),
       (ST_SetSRID(ST_MakePoint(128.413010, 36.108010), 4326), 2),
       (ST_SetSRID(ST_MakePoint(128.413020, 36.108020), 4326), 2);
-- 2. 반경 20m 안에 3점이 존재하는 경우 (왕벚나무)
INSERT INTO species_pos (pos, park_species_id)
VALUES (ST_SetSRID(ST_MakePoint(128.413000, 36.108000), 4326), 3),
       (ST_SetSRID(ST_MakePoint(128.413010, 36.108010), 4326), 3),
       (ST_SetSRID(ST_MakePoint(128.413020, 36.108020), 4326), 3);

-- 3. 반경 100m 안에 2번 조건을 만족하는 점이 무수히 많은 경우 (참새)
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
-- WHERE s.name = '청설모'
--   AND ST_Equals(sp.pos, ST_SetSRID(ST_MakePoint(128.416000, 36.107000), 4326));

-- Exploration Data Insertion (탐험 데이터 삽입)
INSERT INTO exploration (user_id, park_id, start_time, end_time, distance, steps, startdate,
                         image_url, route, completed)
VALUES (1, 1, '2024-09-23 10:00:00', '2024-09-23 12:00:00', 2500, 5000, '2024-09-23',
        'https://i.ibb.co/jLsXS5z/DALL-E-2024-09-25-12-52-09-A-detailed-3-D-like-rendering-of-a-cute-rabbit-standing-on-a-white-backgr.webp',
        ST_GeomFromText(
                'LINESTRING(128.416000 36.107000, 128.416100 36.107100, 128.416200 36.107200)',
                4326), true),
       (1, 2, '2024-09-24 09:00:00', '2024-09-24 11:30:00', 5000, 8000, '2024-09-24',
        'https://i.ibb.co/jLsXS5z/DALL-E-2024-09-25-12-52-09-A-detailed-3-D-like-rendering-of-a-cute-rabbit-standing-on-a-white-backgr.webp',
        ST_GeomFromText(
                'LINESTRING(128.402000 36.100000, 128.402100 36.100100, 128.402500 36.095000)',
                4326), true),
       (1, 3, '2024-09-25 14:00:00', '2024-09-25 16:00:00', 3000, 6000, '2024-09-25',
        'https://i.ibb.co/jLsXS5z/DALL-E-2024-09-25-12-52-09-A-detailed-3-D-like-rendering-of-a-cute-rabbit-standing-on-a-white-backgr.webp',
        ST_GeomFromText(
                'LINESTRING(128.311500 36.119000, 128.311600 36.119100, 128.312000 36.120000)',
                4326), true);

-- Discovery Data Insertion (발견 데이터 삽입)
-- 청설모 발견 데이터
INSERT INTO discovery (user_id, species_id, species_pos_id, discovery_time, image_url)
SELECT 1,                                                                                                                                   -- user_id (테스트사용자1)
       1,                                                                                                                                   -- species_id ('청설모')
       sp.id,                                                                                                                               -- species_pos_id
       '2024-09-23 10:30:00',                                                                                                               -- discovery_time
       'https://i.ibb.co/jLsXS5z/DALL-E-2024-09-25-12-52-09-A-detailed-3-D-like-rendering-of-a-cute-rabbit-standing-on-a-white-backgr.webp' -- image_url (청설모 이미지)
FROM species_pos sp
         JOIN park_species ps ON ps.id = sp.park_species_id
WHERE ps.park_id = 1    -- '싸피 뒷뜰'
  AND ps.species_id = 1 -- 청설모
  AND ST_Equals(sp.pos, ST_SetSRID(ST_MakePoint(128.416000, 36.107000), 4326));

-- 왕벚나무 발견 데이터
INSERT INTO discovery (user_id, species_id, species_pos_id, discovery_time, image_url)
SELECT 1,                                                                                                                                   -- user_id (테스트사용자1)
       2,                                                                                                                                   -- species_id ('왕벚나무')
       sp.id,                                                                                                                               -- species_pos_id
       '2024-09-23 11:00:00',                                                                                                               -- discovery_time
       'https://i.ibb.co/jLsXS5z/DALL-E-2024-09-25-12-52-09-A-detailed-3-D-like-rendering-of-a-cute-rabbit-standing-on-a-white-backgr.webp' -- image_url (왕벚나무 이미지)
FROM species_pos sp
         JOIN park_species ps ON ps.id = sp.park_species_id
WHERE ps.park_id = 1    -- '싸피 뒷뜰'
  AND ps.species_id = 2 -- 왕벚나무
  AND ST_Equals(sp.pos, ST_SetSRID(ST_MakePoint(128.416200, 36.107200), 4326));

-- 잉어 발견 데이터
INSERT INTO discovery (user_id, species_id, species_pos_id, discovery_time, image_url)
SELECT 1,                                                                                                                                   -- user_id (테스트사용자2)
       3,                                                                                                                                   -- species_id ('잉어')
       sp.id,                                                                                                                               -- species_pos_id
       '2024-09-24 10:15:00',                                                                                                               -- discovery_time
       'https://i.ibb.co/jLsXS5z/DALL-E-2024-09-25-12-52-09-A-detailed-3-D-like-rendering-of-a-cute-rabbit-standing-on-a-white-backgr.webp' -- image_url (잉어 이미지)
FROM species_pos sp
         JOIN park_species ps ON ps.id = sp.park_species_id
WHERE ps.park_id = 2    -- 동락공원
  AND ps.species_id = 3 -- 잉어
  AND ST_Equals(sp.pos, ST_SetSRID(ST_MakePoint(128.402500, 36.095000), 4326));

-- 단풍나무 발견 데이터
INSERT INTO discovery (user_id, species_id, species_pos_id, discovery_time, image_url)
SELECT 1,                                                                                                                                   -- user_id (테스트사용자2)
       5,                                                                                                                                   -- species_id ('단풍나무')
       sp.id,                                                                                                                               -- species_pos_id
       '2024-09-24 11:00:00',                                                                                                               -- discovery_time
       'https://i.ibb.co/jLsXS5z/DALL-E-2024-09-25-12-52-09-A-detailed-3-D-like-rendering-of-a-cute-rabbit-standing-on-a-white-backgr.webp' -- image_url (단풍나무 이미지)
FROM species_pos sp
         JOIN park_species ps ON ps.id = sp.park_species_id
WHERE ps.park_id = 2    -- 동락공원
  AND ps.species_id = 5 -- 단풍나무
  AND ST_Equals(sp.pos, ST_SetSRID(ST_MakePoint(128.403000, 36.090000), 4326));

-- 개구리 발견 데이터
INSERT INTO discovery (user_id, species_id, species_pos_id, discovery_time, image_url)
SELECT 1,                                                                                                                                   -- user_id (테스트사용자1)
       6,                                                                                                                                   -- species_id ('개구리')
       sp.id,                                                                                                                               -- species_pos_id
       '2024-09-25 15:00:00',                                                                                                               -- discovery_time
       'https://i.ibb.co/jLsXS5z/DALL-E-2024-09-25-12-52-09-A-detailed-3-D-like-rendering-of-a-cute-rabbit-standing-on-a-white-backgr.webp' -- image_url (개구리 이미지)
FROM species_pos sp
         JOIN park_species ps ON ps.id = sp.park_species_id
WHERE ps.park_id = 3    -- 환경 연수원
  AND ps.species_id = 6 -- 개구리
  AND ST_Equals(sp.pos, ST_SetSRID(ST_MakePoint(128.312000, 36.120000), 4326));



-- ========================================================공원 더미 데이터
-- 테스트용 공원 20개 추가
INSERT INTO park (name, description, image_url)
VALUES ('Test Park 1', '테스트 공원 1 설명', 'https://example.com/test_park1.jpg'),
       ('Test Park 2', '테스트 공원 2 설명', 'https://example.com/test_park2.jpg'),
       ('Test Park 3', '테스트 공원 3 설명', 'https://example.com/test_park3.jpg'),
       ('Test Park 4', '테스트 공원 4 설명', 'https://example.com/test_park4.jpg'),
       ('Test Park 5', '테스트 공원 5 설명', 'https://example.com/test_park5.jpg'),
       ('Test Park 6', '테스트 공원 6 설명', 'https://example.com/test_park6.jpg'),
       ('Test Park 7', '테스트 공원 7 설명', 'https://example.com/test_park7.jpg'),
       ('Test Park 8', '테스트 공원 8 설명', 'https://example.com/test_park8.jpg'),
       ('Test Park 9', '테스트 공원 9 설명', 'https://example.com/test_park9.jpg'),
       ('Test Park 10', '테스트 공원 10 설명', 'https://example.com/test_park10.jpg'),
       ('Test Park 11', '테스트 공원 11 설명', 'https://example.com/test_park11.jpg'),
       ('Test Park 12', '테스트 공원 12 설명', 'https://example.com/test_park12.jpg'),
       ('Test Park 13', '테스트 공원 13 설명', 'https://example.com/test_park13.jpg'),
       ('Test Park 14', '테스트 공원 14 설명', 'https://example.com/test_park14.jpg'),
       ('Test Park 15', '테스트 공원 15 설명', 'https://example.com/test_park15.jpg'),
       ('Test Park 16', '테스트 공원 16 설명', 'https://example.com/test_park16.jpg'),
       ('Test Park 17', '테스트 공원 17 설명', 'https://example.com/test_park17.jpg'),
       ('Test Park 18', '테스트 공원 18 설명', 'https://example.com/test_park18.jpg'),
       ('Test Park 19', '테스트 공원 19 설명', 'https://example.com/test_park19.jpg'),
       ('Test Park 20', '테스트 공원 20 설명', 'https://example.com/test_park20.jpg');

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