--
-- PostgreSQL database dump
--

-- Dumped from database version 16.4
-- Dumped by pg_dump version 16.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: postgis; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA public;


--
-- Name: EXTENSION postgis; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION postgis IS 'PostGIS geometry and geography spatial types and functions';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: chat; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.chat (
    id bigint NOT NULL,
    npc_pos_id bigint NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE public.chat OWNER TO postgres;

--
-- Name: chat_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.chat_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.chat_id_seq OWNER TO postgres;

--
-- Name: chat_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.chat_id_seq OWNED BY public.chat.id;


--
-- Name: chat_messages; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.chat_messages (
    chat_id bigint NOT NULL,
    messages_id bigint NOT NULL
);


ALTER TABLE public.chat_messages OWNER TO postgres;

--
-- Name: discovery; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.discovery (
    discovery_time timestamp(6) without time zone,
    id bigint NOT NULL,
    species_id bigint,
    species_pos_id bigint,
    user_id bigint,
    image_url character varying(255)
);


ALTER TABLE public.discovery OWNER TO postgres;

--
-- Name: discovery_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.discovery_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.discovery_id_seq OWNER TO postgres;

--
-- Name: discovery_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.discovery_id_seq OWNED BY public.discovery.id;


--
-- Name: exploration; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.exploration (
    completed boolean NOT NULL,
    distance double precision NOT NULL,
    startdate date,
    steps integer,
    end_time timestamp(6) without time zone,
    exploration_id bigint NOT NULL,
    park_id bigint,
    start_time timestamp(6) without time zone NOT NULL,
    user_id bigint,
    image_url character varying(512),
    route public.geometry(LineString,4326)
);


ALTER TABLE public.exploration OWNER TO postgres;

--
-- Name: exploration_discoveries; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.exploration_discoveries (
    discoveries_id bigint NOT NULL,
    exploration_exploration_id bigint NOT NULL
);


ALTER TABLE public.exploration_discoveries OWNER TO postgres;

--
-- Name: exploration_exploration_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.exploration_exploration_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.exploration_exploration_id_seq OWNER TO postgres;

--
-- Name: exploration_exploration_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.exploration_exploration_id_seq OWNED BY public.exploration.exploration_id;


--
-- Name: message; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.message (
    user_message boolean NOT NULL,
    id bigint NOT NULL,
    message_time timestamp(6) without time zone NOT NULL,
    content text NOT NULL
);


ALTER TABLE public.message OWNER TO postgres;

--
-- Name: message_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.message_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.message_id_seq OWNER TO postgres;

--
-- Name: message_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.message_id_seq OWNED BY public.message.id;


--
-- Name: native; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.native
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.native OWNER TO postgres;

--
-- Name: npc; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.npc (
    npc_id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE public.npc OWNER TO postgres;

--
-- Name: npc_npc_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.npc_npc_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.npc_npc_id_seq OWNER TO postgres;

--
-- Name: npc_npc_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.npc_npc_id_seq OWNED BY public.npc.npc_id;


--
-- Name: npc_pos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.npc_pos (
    id bigint NOT NULL,
    park_npc_id bigint,
    pos public.geometry(Point,4326) NOT NULL
);


ALTER TABLE public.npc_pos OWNER TO postgres;

--
-- Name: npc_pos_chats; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.npc_pos_chats (
    chats_id bigint NOT NULL,
    npc_pos_id bigint NOT NULL
);


ALTER TABLE public.npc_pos_chats OWNER TO postgres;

--
-- Name: npc_pos_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.npc_pos_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.npc_pos_id_seq OWNER TO postgres;

--
-- Name: npc_pos_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.npc_pos_id_seq OWNED BY public.npc_pos.id;


--
-- Name: npc_pos_quests; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.npc_pos_quests (
    npc_pos_id bigint NOT NULL,
    quest_id bigint
);


ALTER TABLE public.npc_pos_quests OWNER TO postgres;

--
-- Name: park; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.park (
    id bigint NOT NULL,
    description character varying(1024),
    image_url character varying(255),
    name character varying(255) NOT NULL
);


ALTER TABLE public.park OWNER TO postgres;

--
-- Name: park_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.park_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.park_id_seq OWNER TO postgres;

--
-- Name: park_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.park_id_seq OWNED BY public.park.id;


--
-- Name: park_npc; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.park_npc (
    id bigint NOT NULL,
    npc_id bigint,
    park_id bigint
);


ALTER TABLE public.park_npc OWNER TO postgres;

--
-- Name: park_npc_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.park_npc_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.park_npc_id_seq OWNER TO postgres;

--
-- Name: park_npc_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.park_npc_id_seq OWNED BY public.park_npc.id;


--
-- Name: park_pos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.park_pos (
    id bigint NOT NULL,
    park_id bigint,
    name character varying(255),
    pos public.geometry(Point,4326) NOT NULL
);


ALTER TABLE public.park_pos OWNER TO postgres;

--
-- Name: park_pos_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.park_pos_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.park_pos_id_seq OWNER TO postgres;

--
-- Name: park_pos_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.park_pos_id_seq OWNED BY public.park_pos.id;


--
-- Name: park_species; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.park_species (
    id bigint NOT NULL,
    park_id bigint,
    species_id bigint
);


ALTER TABLE public.park_species OWNER TO postgres;

--
-- Name: park_species_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.park_species_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.park_species_id_seq OWNER TO postgres;

--
-- Name: park_species_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.park_species_id_seq OWNED BY public.park_species.id;


--
-- Name: quest; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.quest (
    type integer NOT NULL,
    id bigint NOT NULL
);


ALTER TABLE public.quest OWNER TO postgres;

--
-- Name: quest_completed; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.quest_completed (
    completed_at timestamp(6) without time zone,
    exploration_id bigint,
    id bigint NOT NULL,
    npc_pos_id bigint,
    quest_id bigint,
    species_id bigint,
    status character varying(255),
    CONSTRAINT quest_completed_status_check CHECK (((status)::text = ANY ((ARRAY['WAIT'::character varying, 'PROGRESS'::character varying, 'COMPLETE'::character varying])::text[])))
);


ALTER TABLE public.quest_completed OWNER TO postgres;

--
-- Name: quest_completed_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.quest_completed_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.quest_completed_id_seq OWNER TO postgres;

--
-- Name: quest_completed_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.quest_completed_id_seq OWNED BY public.quest_completed.id;


--
-- Name: quest_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.quest_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.quest_id_seq OWNER TO postgres;

--
-- Name: quest_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.quest_id_seq OWNED BY public.quest.id;


--
-- Name: species; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.species (
    species_id bigint NOT NULL,
    description character varying(255),
    image_url character varying(255),
    name character varying(255),
    scientific_name character varying(255)
);


ALTER TABLE public.species OWNER TO postgres;

--
-- Name: species_pos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.species_pos (
    id bigint NOT NULL,
    park_species_id bigint,
    pos public.geometry(Point,4326) NOT NULL
);


ALTER TABLE public.species_pos OWNER TO postgres;

--
-- Name: species_pos_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.species_pos_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.species_pos_id_seq OWNER TO postgres;

--
-- Name: species_pos_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.species_pos_id_seq OWNED BY public.species_pos.id;


--
-- Name: species_seasons; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.species_seasons (
    species_id bigint NOT NULL,
    season character varying(255),
    CONSTRAINT species_seasons_season_check CHECK (((season)::text = ANY ((ARRAY['SPRING'::character varying, 'SUMMER'::character varying, 'AUTUMN'::character varying, 'WINTER'::character varying])::text[])))
);


ALTER TABLE public.species_seasons OWNER TO postgres;

--
-- Name: species_species_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.species_species_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.species_species_id_seq OWNER TO postgres;

--
-- Name: species_species_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.species_species_id_seq OWNED BY public.species.species_id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    user_id bigint NOT NULL,
    profile_image_url character varying(512),
    email character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    oauth_id character varying(255),
    oauth_provider character varying(255)
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: users_chats; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users_chats (
    chats_id bigint NOT NULL,
    users_user_id bigint NOT NULL
);


ALTER TABLE public.users_chats OWNER TO postgres;

--
-- Name: users_discoveries; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users_discoveries (
    discoveries_id bigint NOT NULL,
    users_user_id bigint NOT NULL
);


ALTER TABLE public.users_discoveries OWNER TO postgres;

--
-- Name: users_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_user_id_seq OWNER TO postgres;

--
-- Name: users_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_user_id_seq OWNED BY public.users.user_id;


--
-- Name: chat id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chat ALTER COLUMN id SET DEFAULT nextval('public.chat_id_seq'::regclass);


--
-- Name: discovery id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.discovery ALTER COLUMN id SET DEFAULT nextval('public.discovery_id_seq'::regclass);


--
-- Name: exploration exploration_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exploration ALTER COLUMN exploration_id SET DEFAULT nextval('public.exploration_exploration_id_seq'::regclass);


--
-- Name: message id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.message ALTER COLUMN id SET DEFAULT nextval('public.message_id_seq'::regclass);


--
-- Name: npc npc_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.npc ALTER COLUMN npc_id SET DEFAULT nextval('public.npc_npc_id_seq'::regclass);


--
-- Name: npc_pos id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.npc_pos ALTER COLUMN id SET DEFAULT nextval('public.npc_pos_id_seq'::regclass);


--
-- Name: park id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.park ALTER COLUMN id SET DEFAULT nextval('public.park_id_seq'::regclass);


--
-- Name: park_npc id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.park_npc ALTER COLUMN id SET DEFAULT nextval('public.park_npc_id_seq'::regclass);


--
-- Name: park_pos id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.park_pos ALTER COLUMN id SET DEFAULT nextval('public.park_pos_id_seq'::regclass);


--
-- Name: park_species id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.park_species ALTER COLUMN id SET DEFAULT nextval('public.park_species_id_seq'::regclass);


--
-- Name: quest id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.quest ALTER COLUMN id SET DEFAULT nextval('public.quest_id_seq'::regclass);


--
-- Name: quest_completed id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.quest_completed ALTER COLUMN id SET DEFAULT nextval('public.quest_completed_id_seq'::regclass);


--
-- Name: species species_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.species ALTER COLUMN species_id SET DEFAULT nextval('public.species_species_id_seq'::regclass);


--
-- Name: species_pos id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.species_pos ALTER COLUMN id SET DEFAULT nextval('public.species_pos_id_seq'::regclass);


--
-- Name: users user_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN user_id SET DEFAULT nextval('public.users_user_id_seq'::regclass);


--
-- Data for Name: chat; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.chat (id, npc_pos_id, user_id) FROM stdin;
\.


--
-- Data for Name: chat_messages; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.chat_messages (chat_id, messages_id) FROM stdin;
\.


--
-- Data for Name: discovery; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.discovery (discovery_time, id, species_id, species_pos_id, user_id, image_url) FROM stdin;
\.


--
-- Data for Name: exploration; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.exploration (completed, distance, startdate, steps, end_time, exploration_id, park_id, start_time, user_id, image_url, route) FROM stdin;
t	2500	2024-09-23	5000	2024-09-23 12:00:00	1	1	2024-09-23 10:00:00	1	test-camera.png	0102000020E6100000030000005A643BDF4F0D604004560E2DB20D4240B37BF2B0500D604068B3EA73B50D42400C93A982510D6040CB10C7BAB80D4240
t	5000	2024-09-24	8000	2024-09-24 11:30:00	2	2	2024-09-24 09:00:00	1	test-camera.png	0102000020E610000003000000BE9F1A2FDD0C6040CDCCCCCCCC0C424017B7D100DE0C6040302AA913D00C42407B14AE47E10C60405C8FC2F5280C4240
t	3000	2024-09-25	6000	2024-09-25 16:00:00	3	3	2024-09-25 14:00:00	1	test-camera.png	0102000020E6100000030000008716D9CEF7096040AC1C5A643B0F4240E02D90A0F8096040107A36AB3E0F4240448B6CE7FB0960408FC2F5285C0F4240
\.


--
-- Data for Name: exploration_discoveries; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.exploration_discoveries (discoveries_id, exploration_exploration_id) FROM stdin;
\.


--
-- Data for Name: message; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.message (user_message, id, message_time, content) FROM stdin;
\.


--
-- Data for Name: npc; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.npc (npc_id, name) FROM stdin;
1	수달
2	검사 수달
3	기도하는 수달
4	너구리
5	마법사 너구리
6	병아리
7	거북이
8	갈색 거북이
9	토끼
10	이정표
11	펭귄
12	돛새치
\.


--
-- Data for Name: npc_pos; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.npc_pos (id, park_npc_id, pos) FROM stdin;
1	1	0101000020E61000003306354B550D6040D63B8D66B00D4240
2	2	0101000020E61000003FE72340520D6040B42922BEAB0D4240
3	3	0101000020E610000087814C6E500D60400B778E99A40D4240
4	5	0101000020E6100000BE9F1A2FDD0C6040CDCCCCCCCC0C4240
5	6	0101000020E61000007B14AE47E10C60405C8FC2F5280C4240
6	9	0101000020E61000008716D9CEF7096040AC1C5A643B0F4240
\.


--
-- Data for Name: npc_pos_chats; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.npc_pos_chats (chats_id, npc_pos_id) FROM stdin;
\.


--
-- Data for Name: npc_pos_quests; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.npc_pos_quests (npc_pos_id, quest_id) FROM stdin;
\.


--
-- Data for Name: park; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.park (id, description, image_url, name) FROM stdin;
1	싸피 구미 캠퍼스 기숙사에 위치한 공원으로 봄이 되면 예쁜 꽃들이 핀답니다. 가을이 되면 감도 딸 수 있어요	ssafy.jpg	싸피 뒷뜰
2	아름다운 낙동강을 끼고 있는 넓은 산책로와 자전거 도로를 갖춘 도심 속 힐링 공간이에요. 산책로와 잔디밭이 있어서 마음껏 뛰어놀 수 있어요.\n공원에는 농구장, 축구장, 배구장, 그리고 롤러스케이트장등 여러 가지 운동 시설도 많이 있어요. 시원한 바닥분수도 있어 시원한 여름을 보낼 수 있어요.\n그리고 무료로 자전거를 대여할 수 있으니 자전거 타는 것도 잊지 마세요. 신나는 동락공원을 꼭 방문해보아요!	dongrac.jpg	동락공원
3	금오산 도립공원 구역 내에 위치하고 있는 수련 시설로 수생태체험학습장, 야외학습체험장 등등 다양한 학습 공간이 있어요.	yonsuwon.jpg	환경 연수원
4	구미의 유일한 습지인 지산샛강생태공원은 봄엔 아름다운 벚꽃산책길, 여름엔 연꽃 군락지, 겨울엔 철새 보금자리로 사계절 즐길거리가 많으며, 도심에서 가까운 곳에 위치해 있어 접근성이 좋아요. 천연기념물 큰고니의 최대 서식지로 잘 알려져있어요. 큰고니를 보고싶으면 방문해보세요.	jisan-setgang.jpg	지산샛강생태공원
5	푸른 나무들이 가득한 공원입니다.	park1.jpg	푸른 숲 공원
6	따뜻한 햇살이 비치는 공원입니다.	park2.jpg	햇살 가득한 정원
7	다양한 꽃들이 만발한 향기로운 공원입니다.	park3.jpg	꽃내음 정원
8	맑은 호수를 끼고 있는 아름다운 공원입니다.	park4.jpg	맑은 호수 공원
9	시원한 바람이 불어오는 산책로입니다.	park5.jpg	바람길 산책로
10	밤에는 별이 잘 보이는 공원입니다.	park6.jpg	별빛 공원
11	산책하며 여유를 즐길 수 있는 조용한 숲길입니다.	park7.jpg	조용한 숲길
12	강을 따라 걸을 수 있는 산책로가 있는 공원입니다.	park8.jpg	강변 공원
13	햇빛이 잘 드는 공간에서 여유를 즐길 수 있는 공원입니다.	park9.jpg	햇빛마루 공원
14	솔향기가 가득한 소나무 숲 공원입니다.	park10.jpg	솔향기 공원
15	다양한 동식물을 만날 수 있는 자연 생태 공원입니다.	park11.jpg	자연 생태원
16	산들바람이 불어오는 정원입니다.	park12.jpg	바람숲 정원
17	따뜻한 햇살을 느낄 수 있는 나무숲 공원입니다.	park13.jpg	햇살숲 공원
18	작은 연못과 물길이 흐르는 정원입니다.	park14.jpg	물빛 정원
19	푸른 언덕을 오르며 자연을 느낄 수 있는 공원입니다.	park15.jpg	푸른 언덕 공원
20	평온한 분위기 속에서 산책할 수 있는 숲길입니다.	park16.jpg	평화의 숲길
21	새벽에 가장 아름다운 풍경을 볼 수 있는 공원입니다.	park17.jpg	새벽공원
22	밤에는 달빛이 비치는 낭만적인 공원입니다.	park18.jpg	달빛 공원
23	넓은 잔디밭에서 여유를 즐길 수 있는 공원입니다.	park19.jpg	잔디밭 공원
24	호숫가를 따라 걸을 수 있는 산책로가 있는 공원입니다.	park20.jpg	호숫가 산책로
\.


--
-- Data for Name: park_npc; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.park_npc (id, npc_id, park_id) FROM stdin;
1	9	1
2	3	1
3	11	1
4	3	2
5	4	2
6	8	2
7	11	2
8	5	3
9	6	3
10	9	3
11	12	3
\.


--
-- Data for Name: park_pos; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.park_pos (id, park_id, name, pos) FROM stdin;
1	1	정문	0101000020E610000012C2A38D230D6040C11BD2A8C00D4240
2	1	후문	0101000020E610000078279F1E5B0D60407216F6B4C30D4240
3	1	개구멍	0101000020E6100000E7543200540D6040E4F8A1D2880D4240
4	2	북쪽 입구	0101000020E6100000ADA7565FDD0C6040274A42226D0D4240
5	2	1 주차장	0101000020E610000048895DDBDB0C60406D5512D9070D4240
6	2	중간 입구 1	0101000020E61000008E740646DE0C6040A374E95F920C4240
7	2	중간 입구 2	0101000020E610000093E34EE9E00C60409D0FCF12640C4240
8	2	4 주차장	0101000020E6100000DE3D40F7E50C6040925CFE43FA0B4240
9	2	중간 입구	0101000020E61000007E8E8F16E70C6040A31F0DA7CC0B4240
10	2	5 주차장	0101000020E61000006075E448E70C604031276893C30B4240
11	2	중간 입구	0101000020E6100000C1E5B166E40C60407BD80B056C0B4240
12	2	6 주차장	0101000020E6100000C7BC8E38E40C604091477023650B4240
13	2	아래 입구	0101000020E61000001827BEDAD10C6040A6608DB3E90A4240
14	2	7 주차장	0101000020E6100000643F8BA5C80C60407D76C075C50A4240
15	2	젤 아래 입구	0101000020E61000008B19E1EDC10C60406CD102B4AD0A4240
16	3	환경 연수원 입구	0101000020E61000005AA148F7F3096040E9F010C64F0F4240
17	4	지산샛강 생태공원 입구	0101000020E61000001381EA1F440B6040FDD98F1491114240
18	5	입구	0101000020E6100000339AA1BB931760404DD7816EEDFB4140
19	6	입구	0101000020E61000004487B94E4B1460400686FB864F2A4240
20	7	입구	0101000020E6100000DA0BBD42000F60409EF45C2E7FE14140
21	8	입구	0101000020E610000082BEEDE20910604034F63EA1D4F64140
22	9	입구	0101000020E6100000C77449166D1860406DBD874368F74140
23	10	입구	0101000020E6100000786F10387416604060A5634AF5F34140
24	11	입구	0101000020E610000011B0C181F517604021400DD86DF64140
25	12	입구	0101000020E61000007ACDE1EF3A096040315AF02C5F0E4240
26	13	입구	0101000020E6100000C7D94665C4026040AAD73E23F10D4240
27	14	입구	0101000020E610000042D048E37A1360407B4C4F0FE9E74140
28	15	입구	0101000020E61000008322751706096040CFEBDBC525234240
29	16	입구	0101000020E6100000CFE20D0DB30E6040E148A7F9BF0B4240
30	17	입구	0101000020E61000000674D2A0530E604082E11AE240164240
31	18	입구	0101000020E61000009815A638F0136040B41E21E20A284240
32	19	입구	0101000020E610000042C12863011160404D1E489E18324240
33	20	입구	0101000020E610000003206A71C8076040E76E4FA7C3174240
34	21	입구	0101000020E61000006B39A4A5AA106040B782A8AD4A074240
35	22	입구	0101000020E6100000C111F885C40D60403322B13151014240
36	23	입구	0101000020E6100000A26C590FFF06604037E5DF119FF94140
37	24	입구	0101000020E61000007D8ECB2D0518604030828E4DEBF44140
\.


--
-- Data for Name: park_species; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.park_species (id, park_id, species_id) FROM stdin;
1	1	1
2	1	2
3	1	3
4	2	1
5	2	2
6	2	3
7	2	8
8	2	16
9	3	1
10	3	2
11	3	3
12	3	8
13	3	16
\.


--
-- Data for Name: quest; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.quest (type, id) FROM stdin;
1	1
2	2
3	3
\.


--
-- Data for Name: quest_completed; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.quest_completed (completed_at, exploration_id, id, npc_pos_id, quest_id, species_id, status) FROM stdin;
\.


--
-- Data for Name: spatial_ref_sys; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.spatial_ref_sys (srid, auth_name, auth_srid, srtext, proj4text) FROM stdin;
\.


--
-- Data for Name: species; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.species (species_id, description, image_url, name, scientific_name) FROM stdin;
1	강아지 꼬리처럼 복슬복슬 귀여운 강아지풀	foxtail.jpg	강아지풀	Setaria viridis
2	가을이 되면 물드는 단풍잎	maple-leaves.jpg	단풍잎	Acer palmatum
3	씨앗이 들어있는 자그마한 비늘들이 둥글게 모인 소나무 열매의 송이	pine-cones.jpg	솔방울	Pinus densiflora Siebold & Zucc
4	다양한 색깔의 꽃을 피우는 관목	lantana.jpg	란타나	Lantana camara
5	큰 꽃을 피우는 열대성 식물	hibiscus.jpg	히비스커스	Hibiscus rosa-sinensis
6	고소한 향을 가진 꽃을 피우는 나무	jatropha-curcas.jpg	야트로파 쿠르카스	Jatropha curcas
7	주황색 꽃을 피우는 한해살이 식물	marigold.jpg	마리골드	Tagetes erecta
8	전 세계적으로 사랑받는 꽃으로 다양한 품종이 존재	rose.jpg	장미	Rosa
9	향기로운 꽃을 피우는 나무	champaca.jpg	참파카초령목	Magnolia champaca
10	작은 흰색 꽃을 피우는 관목	plumbago-zeylanica.jpg	백화단	Plumbago zeylanica
11	달콤한 향을 가진 덩굴식물	lonicera.jpg	인동덩굴	Lonicera
12	작은 노란 꽃을 피우는 식물	abutilon-indicum.jpg	아부틸론 인디쿰	Abutilon indicum
13	보라색 꽃을 피우는 관목	melastoma-malabathricum.jpg	야모란과	Melastoma malabathricum
14	파란색 꽃을 피우는 덩굴식물	clitoria-ternatea.jpg	버터플라이피	Clitoria ternatea
15	거미 모양의 꽃을 피우는 식물	hymenocallis-littoralis.jpg	히메노칼리스	Hymenocallis littoralis
16	커다란 노란 꽃을 피우는 식물	sunflowers.jpg	해바라기	Helianthus annuus
17	다양한 색과 모양을 가진 아름다운 날개를 펼치며 꽃가루를 모으는 곤충	butterfly.jpg	나비	butterfly
18	빠른 비행과 날렵한 체형을 가진 곤충으로 주로 습지나 물가에서 볼 수 있음	dragonfly.jpg	잠자리	dragonfly
19	커다란 뿔과 강력한 턱을 가진 곤충으로, 주로 숲 속에서 서식	stag-beetle.jpg	사슴벌레	stag beetle
\.


--
-- Data for Name: species_pos; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.species_pos (id, park_species_id, pos) FROM stdin;
1	1	0101000020E61000004AE320EC540D60405091CAC5B90D4240
2	1	0101000020E610000034E80D90550D604043C1FB51BA0D4240
3	2	0101000020E610000099433E34510D6040C937997CBC0D4240
4	2	0101000020E6100000F04EE753520D6040DCECA79FBC0D4240
5	2	0101000020E61000004B438594540D60401C1F1790BA0D4240
6	3	0101000020E6100000D76EBAA54D0D60409AA62634B00D4240
7	3	0101000020E6100000195104A54D0D60409584D2B6B10D4240
8	3	0101000020E6100000028315EF4D0D60409DD5CC11B90D4240
\.


--
-- Data for Name: species_seasons; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.species_seasons (species_id, season) FROM stdin;
1	SPRING
1	SUMMER
2	AUTUMN
3	SPRING
3	SUMMER
3	AUTUMN
4	SPRING
4	SUMMER
4	AUTUMN
5	SPRING
5	SUMMER
5	AUTUMN
6	SPRING
6	SUMMER
7	SPRING
7	SUMMER
7	AUTUMN
8	SPRING
8	SUMMER
8	AUTUMN
8	WINTER
9	SPRING
9	SUMMER
10	SPRING
10	SUMMER
11	SPRING
12	SPRING
12	SUMMER
13	SPRING
13	SUMMER
13	AUTUMN
14	SUMMER
14	AUTUMN
15	SUMMER
15	AUTUMN
16	SUMMER
16	AUTUMN
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (user_id, profile_image_url, email, name, oauth_id, oauth_provider) FROM stdin;
1	profile_image_url	ssafy1@naver.com	테스트사용자1	oauth_id1	oauth_provider
2	profile_image_url	ssafy2@naver.com	테스트사용자2	oauth_id2	oauth_provider
\.


--
-- Data for Name: users_chats; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users_chats (chats_id, users_user_id) FROM stdin;
\.


--
-- Data for Name: users_discoveries; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users_discoveries (discoveries_id, users_user_id) FROM stdin;
\.


--
-- Name: chat_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.chat_id_seq', 1, false);


--
-- Name: discovery_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.discovery_id_seq', 1, false);


--
-- Name: exploration_exploration_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.exploration_exploration_id_seq', 3, true);


--
-- Name: message_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.message_id_seq', 1, false);


--
-- Name: native; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.native', 1, false);


--
-- Name: npc_npc_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.npc_npc_id_seq', 12, true);


--
-- Name: npc_pos_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.npc_pos_id_seq', 6, true);


--
-- Name: park_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.park_id_seq', 24, true);


--
-- Name: park_npc_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.park_npc_id_seq', 11, true);


--
-- Name: park_pos_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.park_pos_id_seq', 37, true);


--
-- Name: park_species_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.park_species_id_seq', 13, true);


--
-- Name: quest_completed_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.quest_completed_id_seq', 1, false);


--
-- Name: quest_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.quest_id_seq', 3, true);


--
-- Name: species_pos_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.species_pos_id_seq', 8, true);


--
-- Name: species_species_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.species_species_id_seq', 19, true);


--
-- Name: users_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_user_id_seq', 2, true);


--
-- Name: chat_messages chat_messages_messages_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chat_messages
    ADD CONSTRAINT chat_messages_messages_id_key UNIQUE (messages_id);


--
-- Name: chat chat_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chat
    ADD CONSTRAINT chat_pkey PRIMARY KEY (id);


--
-- Name: discovery discovery_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.discovery
    ADD CONSTRAINT discovery_pkey PRIMARY KEY (id);


--
-- Name: exploration_discoveries exploration_discoveries_discoveries_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exploration_discoveries
    ADD CONSTRAINT exploration_discoveries_discoveries_id_key UNIQUE (discoveries_id);


--
-- Name: exploration exploration_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exploration
    ADD CONSTRAINT exploration_pkey PRIMARY KEY (exploration_id);


--
-- Name: message message_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.message
    ADD CONSTRAINT message_pkey PRIMARY KEY (id);


--
-- Name: npc npc_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.npc
    ADD CONSTRAINT npc_pkey PRIMARY KEY (npc_id);


--
-- Name: npc_pos_chats npc_pos_chats_chats_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.npc_pos_chats
    ADD CONSTRAINT npc_pos_chats_chats_id_key UNIQUE (chats_id);


--
-- Name: npc_pos npc_pos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.npc_pos
    ADD CONSTRAINT npc_pos_pkey PRIMARY KEY (id);


--
-- Name: park_npc park_npc_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.park_npc
    ADD CONSTRAINT park_npc_pkey PRIMARY KEY (id);


--
-- Name: park park_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.park
    ADD CONSTRAINT park_pkey PRIMARY KEY (id);


--
-- Name: park_pos park_pos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.park_pos
    ADD CONSTRAINT park_pos_pkey PRIMARY KEY (id);


--
-- Name: park_species park_species_park_id_species_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.park_species
    ADD CONSTRAINT park_species_park_id_species_id_key UNIQUE (park_id, species_id);


--
-- Name: park_species park_species_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.park_species
    ADD CONSTRAINT park_species_pkey PRIMARY KEY (id);


--
-- Name: quest_completed quest_completed_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.quest_completed
    ADD CONSTRAINT quest_completed_pkey PRIMARY KEY (id);


--
-- Name: quest quest_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.quest
    ADD CONSTRAINT quest_pkey PRIMARY KEY (id);


--
-- Name: species species_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.species
    ADD CONSTRAINT species_pkey PRIMARY KEY (species_id);


--
-- Name: species_pos species_pos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.species_pos
    ADD CONSTRAINT species_pos_pkey PRIMARY KEY (id);


--
-- Name: users_chats users_chats_chats_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_chats
    ADD CONSTRAINT users_chats_chats_id_key UNIQUE (chats_id);


--
-- Name: users_discoveries users_discoveries_discoveries_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_discoveries
    ADD CONSTRAINT users_discoveries_discoveries_id_key UNIQUE (discoveries_id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);


--
-- Name: users_chats fk2socjnv4s35rm535cn1lfx2c1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_chats
    ADD CONSTRAINT fk2socjnv4s35rm535cn1lfx2c1 FOREIGN KEY (chats_id) REFERENCES public.chat(id);


--
-- Name: quest_completed fk2t0mhcao39q1e72dktemka7mg; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.quest_completed
    ADD CONSTRAINT fk2t0mhcao39q1e72dktemka7mg FOREIGN KEY (quest_id) REFERENCES public.quest(id);


--
-- Name: npc_pos_chats fk3y3sivobybgyef2imbhkeu48h; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.npc_pos_chats
    ADD CONSTRAINT fk3y3sivobybgyef2imbhkeu48h FOREIGN KEY (chats_id) REFERENCES public.chat(id);


--
-- Name: discovery fk53t28t3gesl9kpk14xemy9677; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.discovery
    ADD CONSTRAINT fk53t28t3gesl9kpk14xemy9677 FOREIGN KEY (species_id) REFERENCES public.species(species_id);


--
-- Name: users_chats fk5j8emf6gufgfbqc5toasyec4l; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_chats
    ADD CONSTRAINT fk5j8emf6gufgfbqc5toasyec4l FOREIGN KEY (users_user_id) REFERENCES public.users(user_id);


--
-- Name: users_discoveries fk758lnm6pw5ihebd2jpc8wymb9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_discoveries
    ADD CONSTRAINT fk758lnm6pw5ihebd2jpc8wymb9 FOREIGN KEY (users_user_id) REFERENCES public.users(user_id);


--
-- Name: npc_pos fk7gbhdjeutq08npjmm5h5uelh1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.npc_pos
    ADD CONSTRAINT fk7gbhdjeutq08npjmm5h5uelh1 FOREIGN KEY (park_npc_id) REFERENCES public.park_npc(id);


--
-- Name: exploration_discoveries fk98iufarv6ly40lnedvm2079cd; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exploration_discoveries
    ADD CONSTRAINT fk98iufarv6ly40lnedvm2079cd FOREIGN KEY (exploration_exploration_id) REFERENCES public.exploration(exploration_id);


--
-- Name: park_npc fk9lhv8fmrc5euolcbo82dw9lo7; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.park_npc
    ADD CONSTRAINT fk9lhv8fmrc5euolcbo82dw9lo7 FOREIGN KEY (park_id) REFERENCES public.park(id);


--
-- Name: species_pos fk9mjoh454mofpldmrxndvgo97y; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.species_pos
    ADD CONSTRAINT fk9mjoh454mofpldmrxndvgo97y FOREIGN KEY (park_species_id) REFERENCES public.park_species(id);


--
-- Name: exploration fkab7ramiygw2tuihkbb2y98jbf; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exploration
    ADD CONSTRAINT fkab7ramiygw2tuihkbb2y98jbf FOREIGN KEY (park_id) REFERENCES public.park(id);


--
-- Name: chat_messages fkb27mi3082eolv7k6tavhgq3wc; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chat_messages
    ADD CONSTRAINT fkb27mi3082eolv7k6tavhgq3wc FOREIGN KEY (chat_id) REFERENCES public.chat(id);


--
-- Name: npc_pos_chats fkbrwsl71tk1gaa3x8orb5acamd; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.npc_pos_chats
    ADD CONSTRAINT fkbrwsl71tk1gaa3x8orb5acamd FOREIGN KEY (npc_pos_id) REFERENCES public.npc_pos(id);


--
-- Name: species_seasons fkcei1k34wq7pbkmegsie2mpkxn; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.species_seasons
    ADD CONSTRAINT fkcei1k34wq7pbkmegsie2mpkxn FOREIGN KEY (species_id) REFERENCES public.species(species_id);


--
-- Name: park_species fkclfv8w9eadm56hsgl0v8v6bei; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.park_species
    ADD CONSTRAINT fkclfv8w9eadm56hsgl0v8v6bei FOREIGN KEY (species_id) REFERENCES public.species(species_id);


--
-- Name: park_pos fkdfa6hbch74p2ohodjd0vf9erk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.park_pos
    ADD CONSTRAINT fkdfa6hbch74p2ohodjd0vf9erk FOREIGN KEY (park_id) REFERENCES public.park(id);


--
-- Name: quest_completed fkdsg9fvcgsgpl0wfuvu8ej5st2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.quest_completed
    ADD CONSTRAINT fkdsg9fvcgsgpl0wfuvu8ej5st2 FOREIGN KEY (npc_pos_id) REFERENCES public.npc_pos(id);


--
-- Name: chat fkebheaix74x3j8gc90dynlg1jv; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chat
    ADD CONSTRAINT fkebheaix74x3j8gc90dynlg1jv FOREIGN KEY (npc_pos_id) REFERENCES public.npc_pos(id);


--
-- Name: exploration fkfqq3c38cqd499u9b0g1va0iu5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exploration
    ADD CONSTRAINT fkfqq3c38cqd499u9b0g1va0iu5 FOREIGN KEY (user_id) REFERENCES public.users(user_id);


--
-- Name: park_species fkipyen7ne1gc1a6oosgsinubr7; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.park_species
    ADD CONSTRAINT fkipyen7ne1gc1a6oosgsinubr7 FOREIGN KEY (park_id) REFERENCES public.park(id);


--
-- Name: chat_messages fkjtlh6un2reea4nsgktq7qtao0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chat_messages
    ADD CONSTRAINT fkjtlh6un2reea4nsgktq7qtao0 FOREIGN KEY (messages_id) REFERENCES public.message(id);


--
-- Name: quest_completed fkkvgxc95iuy774vdmb7a6g1ijt; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.quest_completed
    ADD CONSTRAINT fkkvgxc95iuy774vdmb7a6g1ijt FOREIGN KEY (exploration_id) REFERENCES public.exploration(exploration_id);


--
-- Name: users_discoveries fkmcj3h3t6gcnglvhrjqgithf8e; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_discoveries
    ADD CONSTRAINT fkmcj3h3t6gcnglvhrjqgithf8e FOREIGN KEY (discoveries_id) REFERENCES public.discovery(id);


--
-- Name: park_npc fknpao5vyr9h1fwq4bs9vrpxsj4; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.park_npc
    ADD CONSTRAINT fknpao5vyr9h1fwq4bs9vrpxsj4 FOREIGN KEY (npc_id) REFERENCES public.npc(npc_id);


--
-- Name: discovery fko4pmk4l11u9c6yy2bc1mcrsks; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.discovery
    ADD CONSTRAINT fko4pmk4l11u9c6yy2bc1mcrsks FOREIGN KEY (species_pos_id) REFERENCES public.species_pos(id);


--
-- Name: exploration_discoveries fkpsrgebypmyx73wr56plxlfkoe; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exploration_discoveries
    ADD CONSTRAINT fkpsrgebypmyx73wr56plxlfkoe FOREIGN KEY (discoveries_id) REFERENCES public.discovery(id);


--
-- Name: npc_pos_quests fkr8rqiuu18ydxogcc12x9jxver; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.npc_pos_quests
    ADD CONSTRAINT fkr8rqiuu18ydxogcc12x9jxver FOREIGN KEY (npc_pos_id) REFERENCES public.npc_pos(id);


--
-- PostgreSQL database dump complete
--

