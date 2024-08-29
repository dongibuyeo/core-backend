DROP DATABASE IF EXISTS `DONG`;

CREATE DATABASE `DONG` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;;

USE `DONG`;

CREATE TABLE DONG.`member` (
                               `created_at` datetime(6) DEFAULT NULL,
                               `deleted_at` datetime(6) DEFAULT NULL,
                               `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL ,
                               `updated_at` datetime(6) DEFAULT NULL,
                               `nickname` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                               `id` binary(16) NOT NULL,
                               `device_token` varchar(512) DEFAULT NULL,
                               `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                               `profile_image` varchar(255) DEFAULT NULL,
                               `user_key` varchar(255) DEFAULT NULL,
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE DONG.`account` (
                           `created_at` datetime(6) DEFAULT NULL,
                           `deleted_at` datetime(6) DEFAULT NULL,
                           `updated_at` datetime(6) DEFAULT NULL,
                           `id` binary(16) NOT NULL,
                           `member_id` binary(16) DEFAULT NULL,
                           `account_no` varchar(255) DEFAULT NULL,
                           `account_type` enum('CHALLENGE','PRIVATE') DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           KEY `FKr5j0huynd7nsv1s7e9vb8qvwo` (`member_id`),
                           CONSTRAINT `FKr5j0huynd7nsv1s7e9vb8qvwo` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE DONG.`challenge` (
                             `end_date` date DEFAULT NULL,
                             `participants` int DEFAULT NULL,
                             `start_date` date DEFAULT NULL,
                             `status` tinyint DEFAULT NULL,
                             `created_at` datetime(6) DEFAULT NULL,
                             `deleted_at` datetime(6) DEFAULT NULL,
                             `total_deposit` bigint DEFAULT NULL,
                             `updated_at` datetime(6) DEFAULT NULL,
                             `account_id` binary(16) DEFAULT NULL,
                             `id` binary(16) NOT NULL,
                             `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                             `image` varchar(255) DEFAULT NULL,
                             `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                             `type` enum('CONSUMPTION_COFFEE','CONSUMPTION_DELIVERY','CONSUMPTION_DRINK','QUIZ_SOLBEING','SAVINGS_SEVEN') DEFAULT NULL,
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `UK4saeeac119vkfictd49yb5g67` (`account_id`),
                             CONSTRAINT `FKwbq44q4pl5dp1j6q0y5sjg7y` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
                             CONSTRAINT `challenge_chk_1` CHECK ((`status` between 0 and 2))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE DONG.`member_challenge` (
                                         `is_success` bit(1) DEFAULT NULL,
                                         `total_score` int DEFAULT NULL,
                                         `additional_reward` bigint DEFAULT NULL,
                                         `base_reward` bigint DEFAULT NULL,
                                         `created_at` datetime(6) DEFAULT NULL,
                                         `deleted_at` datetime(6) DEFAULT NULL,
                                         `deposit` bigint DEFAULT NULL,
                                         `updated_at` datetime(6) DEFAULT NULL,
                                         `challenge_id` binary(16) DEFAULT NULL,
                                         `id` binary(16) NOT NULL,
                                         `member_id` binary(16) DEFAULT NULL,
                                         `status` enum('BEFORE_CALCULATION','CALCULATED','REWARDED') DEFAULT NULL,
                                         PRIMARY KEY (`id`),
                                         KEY `FKh9n4f0bidmjun3fvk2jp5netm` (`challenge_id`),
                                         KEY `FK9x810nqdrhsdpp78017y3kqhe` (`member_id`),
                                         CONSTRAINT `FK9x810nqdrhsdpp78017y3kqhe` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`),
                                         CONSTRAINT `FKh9n4f0bidmjun3fvk2jp5netm` FOREIGN KEY (`challenge_id`) REFERENCES `challenge` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE DONG.`consumption` (
                               `created_at` datetime(6) DEFAULT NULL,
                               `deleted_at` datetime(6) DEFAULT NULL,
                               `transaction_after_balance` bigint DEFAULT NULL,
                               `transaction_balance` bigint DEFAULT NULL,
                               `updated_at` datetime(6) DEFAULT NULL,
                               `id` binary(16) NOT NULL,
                               `member_id` binary(16) DEFAULT NULL,
                               `transaction_account_no` varchar(255) DEFAULT NULL,
                               `transaction_date` varchar(255) DEFAULT NULL,
                               `transaction_memo` varchar(255) DEFAULT NULL,
                               `transaction_summary` varchar(255) DEFAULT NULL,
                               `transaction_time` varchar(255) DEFAULT NULL,
                               `transaction_type` varchar(255) DEFAULT NULL,
                               `transaction_type_name` varchar(255) DEFAULT NULL,
                               `transaction_unique_no` varchar(255) DEFAULT NULL,
                               PRIMARY KEY (`id`),
                               KEY `FKra4hpn2ur20o0p56dsvdodj94` (`member_id`),
                               CONSTRAINT `FKra4hpn2ur20o0p56dsvdodj94` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE DONG.`daily_score` (
                               `date` date DEFAULT NULL,
                               `total_score` int NOT NULL,
                               `id` binary(16) NOT NULL,
                               `member_challenge_id` binary(16) DEFAULT NULL,
                               PRIMARY KEY (`id`),
                               KEY `FK36en58l32c8r7ei87cl4gsii3` (`member_challenge_id`),
                               CONSTRAINT `FK36en58l32c8r7ei87cl4gsii3` FOREIGN KEY (`member_challenge_id`) REFERENCES `member_challenge` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE DONG.`room` (
                             `created_at` datetime(6) DEFAULT NULL,
                             `deleted_at` datetime(6) DEFAULT NULL,
                             `updated_at` datetime(6) DEFAULT NULL,
                             `id` binary(16) NOT NULL,
                             `name` varchar(255) DEFAULT NULL,
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE DONG.`message` (
                           `created_at` datetime(6) DEFAULT NULL,
                           `deleted_at` datetime(6) DEFAULT NULL,
                           `updated_at` datetime(6) DEFAULT NULL,
                           `id` binary(16) NOT NULL,
                           `member_id` binary(16) DEFAULT NULL,
                           `room_id` binary(16) DEFAULT NULL,
                           `message` varchar(255) DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           KEY `FK4msccpwmxulmw875edu7p352d` (`member_id`),
                           KEY `FKl1kg5a2471cv6pkew0gdgjrmo` (`room_id`),
                           CONSTRAINT `FK4msccpwmxulmw875edu7p352d` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`),
                           CONSTRAINT `FKl1kg5a2471cv6pkew0gdgjrmo` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE DONG.`product` (
                           `created_at` datetime(6) DEFAULT NULL,
                           `deleted_at` datetime(6) DEFAULT NULL,
                           `updated_at` datetime(6) DEFAULT NULL,
                           `id` binary(16) NOT NULL,
                           `account_description` varchar(255) DEFAULT NULL,
                           `account_name` varchar(255) DEFAULT NULL,
                           `account_type` varchar(255) DEFAULT NULL,
                           `account_type_code` varchar(255) DEFAULT NULL,
                           `account_type_name` varchar(255) DEFAULT NULL,
                           `account_type_unique_no` varchar(255) DEFAULT NULL,
                           `bank_code` varchar(255) DEFAULT NULL,
                           `bank_name` varchar(255) DEFAULT NULL,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE DONG.`quiz` (
                        `answer` bit(1) DEFAULT NULL,
                        `created_at` datetime(6) DEFAULT NULL,
                        `deleted_at` datetime(6) DEFAULT NULL,
                        `updated_at` datetime(6) DEFAULT NULL,
                        `id` binary(16) NOT NULL,
                        `question` varchar(255) DEFAULT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE DONG.`quiz_member` (
                               `created_at` datetime(6) DEFAULT NULL,
                               `deleted_at` datetime(6) DEFAULT NULL,
                               `solved_at` datetime(6) DEFAULT NULL,
                               `updated_at` datetime(6) DEFAULT NULL,
                               `id` binary(16) NOT NULL,
                               `member_id` binary(16) DEFAULT NULL,
                               `quiz_id` binary(16) DEFAULT NULL,
                               PRIMARY KEY (`id`),
                               KEY `FK12peccx2dtulmtm31oofjbw61` (`member_id`),
                               KEY `FKe5ckx4p03i3utc2a413uaxahr` (`quiz_id`),
                               CONSTRAINT `FK12peccx2dtulmtm31oofjbw61` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`),
                               CONSTRAINT `FKe5ckx4p03i3utc2a413uaxahr` FOREIGN KEY (`quiz_id`) REFERENCES `quiz` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE DONG.`score_entries` (
                                 `current_total_score` int DEFAULT NULL,
                                 `score` int DEFAULT NULL,
                                 `daily_score_id` binary(16) NOT NULL,
                                 `description` varchar(255) DEFAULT NULL,
                                 KEY `FKe23oh1ywc5mgk76qost22vc6q` (`daily_score_id`),
                                 CONSTRAINT `FKe23oh1ywc5mgk76qost22vc6q` FOREIGN KEY (`daily_score_id`) REFERENCES `daily_score` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO DONG.member (id, email, name, nickname, profile_image, user_key, device_token, created_at, updated_at)
VALUES (UNHEX(REPLACE(UUID(), '-', '')), 'example@example.com', 'John', 'johnny', 'profile.png', NULL, 'exampleDeviceToken', NOW(), NOW());

INSERT INTO DONG.room (created_at, deleted_at, updated_at, id, name)
VALUES (NOW(),null,NOW(),UNHEX(REPLACE(UUID(), '-', '')),'COFFEE');

INSERT INTO DONG.room (created_at, deleted_at, updated_at, id, name)
VALUES (NOW(),null,NOW(),UNHEX(REPLACE(UUID(), '-', '')),'DELIVERY');

INSERT INTO DONG.room (created_at, deleted_at, updated_at, id, name)
VALUES (NOW(),null,NOW(),UNHEX(REPLACE(UUID(), '-', '')),'DRINK');

INSERT INTO DONG.room (created_at, deleted_at, updated_at, id, name)
VALUES (NOW(),null,NOW(),UNHEX(REPLACE(UUID(), '-', '')),'SEVEN');

INSERT INTO DONG.room (created_at, deleted_at, updated_at, id, name)
VALUES (NOW(),null,NOW(),UNHEX(REPLACE(UUID(), '-', '')),'QUIZ');


INSERT INTO DONG.member (created_at, deleted_at, name, updated_at, nickname, id, device_token, email, profile_image, user_key) VALUES
('2024-08-29 12:51:42', NULL, 'admin', '2024-08-29 12:51:42', 'admin', 0x01919C42B3CEB4F1A612238F5CE70C00, NULL, 'admin@dongibuyeo-test.com', NULL, '113c59ec-b974-4f04-984f-6642566d4856');
INSERT INTO DONG.member (created_at, deleted_at, name, updated_at, nickname, id, device_token, email, profile_image, user_key) VALUES ('2024-08-29 12:54:45', NULL, '박수진', '2024-08-29 12:54:45', '박박박', 0x01919C457DCC847749819F669AB5DD46, '', 'sujin1@naver.com', NULL, '7e72acce-c2a3-4a67-be11-aa9702156257');
INSERT INTO DONG.member (created_at, deleted_at, name, updated_at, nickname, id, device_token, email, profile_image, user_key) VALUES ('2024-08-29 13:06:37', NULL, '신한', '2024-08-29 14:27:48', '해커톤시렁', 0x01919C505AF68AE77DFFEA6E329C5A6D, '', 'asd@asd.com', 'Lay', '534effa4-1573-4fb0-a857-aea69ca6568d');
INSERT INTO DONG.member (created_at, deleted_at, name, updated_at, nickname, id, device_token, email, profile_image, user_key) VALUES ('2024-08-29 13:23:25', NULL, '김신한', '2024-08-29 13:23:25', 'shinhanKim', 0x01919C5FBAC69C61BAD76CE5F992440E, '', 'shinhanKim@dongibuyeo-test.com', '', '5971b18f-bfd6-49a4-b1c8-883d70ebb803');
INSERT INTO DONG.member (created_at, deleted_at, name, updated_at, nickname, id, device_token, email, profile_image, user_key) VALUES ('2024-08-29 14:53:56', NULL, '곽곽', '2024-08-29 14:53:56', '곽곽이', 0x01919CB29B1033C9DCF879E53C3B31F2, '', 'test123@test.co', NULL, 'cbe35d3c-caed-42ca-8caf-eaa25081ee94');

INSERT INTO DONG.account (created_at, deleted_at, updated_at, id, member_id, account_no, account_type)
VALUES
    ('2024-08-29 13:24:02', NULL, '2024-08-29 13:24:02.964314', 0x01919C604E86334E71442B0E6AA8B8AE, 0x01919C5FBAC69C61BAD76CE5F992440E, '0042401830256381', 'PRIVATE'),
    ('2024-08-29 13:24:35', NULL, '2024-08-29 13:24:35.810119', 0x01919C60CED7C389FB1E29F132E6A5FA, 0x01919C5FBAC69C61BAD76CE5F992440E, '0111427268290839', 'PRIVATE'),
    ('2024-08-29 13:25:02', NULL, '2024-08-29 13:25:02.677679', 0x01919C6137CFF4AAE65FADA7BF87524C, 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', 'PRIVATE'),
    ('2024-08-29 13:26:21', NULL, '2024-08-29 13:26:21.064392', 0x01919C626A023FB66374ACE2C02365A6, 0x01919C5FBAC69C61BAD76CE5F992440E, '0881763003647857', 'PRIVATE'),
    ('2024-08-29 13:31:18', NULL, '2024-08-29 13:31:18.086350', 0x01919C66F23F4D7949CF037FA9AACCD9, 0x01919C5FBAC69C61BAD76CE5F992440E, '0887556547087119', 'CHALLENGE'),
    ('2024-08-29 13:38:40', NULL, '2024-08-29 13:38:40.866248', 0x01919C6DB3CEB83D89143E25FE34ECA3, 0x01919C42B3CEB4F1A612238F5CE70C00, '0889713108612288', 'CHALLENGE'),
    ('2024-08-29 13:39:55', NULL, '2024-08-29 13:39:55.918940', 0x01919C6ED8B9840B50D66E4E37A82A9A, 0x01919C42B3CEB4F1A612238F5CE70C00, '0887447183036205', 'CHALLENGE'),
    ('2024-08-29 13:41:56', NULL, '2024-08-29 13:41:56.579747', 0x01919C70B0576B79B6F6ECA0E5CAAE8D, 0x01919C42B3CEB4F1A612238F5CE70C00, '0888396451871590', 'CHALLENGE'),
    ('2024-08-29 13:43:13', NULL, '2024-08-29 13:43:13.768534', 0x01919C71DDDFF0C092939C6776774211, 0x01919C42B3CEB4F1A612238F5CE70C00, '0885898830115588', 'CHALLENGE'),
    ('2024-08-29 13:44:05', NULL, '2024-08-29 13:44:05.056436', 0x01919C72A6348824743639E75691CB4C, 0x01919C42B3CEB4F1A612238F5CE70C00, '0886860797262912', 'CHALLENGE'),
    ('2024-08-29 13:45:05', NULL, '2024-08-29 13:45:05.886824', 0x01919C739360D4E0E2F99D1F6C6B42E6, 0x01919C42B3CEB4F1A612238F5CE70C00, '0886377511023806', 'CHALLENGE'),
    ('2024-08-29 13:52:09', NULL, '2024-08-29 13:52:09.536775', 0x01919C7A0AB82D56D98ED81CDC14EA3F, 0x01919C5FBAC69C61BAD76CE5F992440E, '0883038929', 'PRIVATE'),
    ('2024-08-29 13:53:43', NULL, '2024-08-29 13:53:43.542246', 0x01919C7B79ED9437CA6870707E44C8F1, 0x01919C5FBAC69C61BAD76CE5F992440E, '0012954192', 'PRIVATE');


INSERT INTO DONG.challenge (end_date, participants, start_date, status, created_at, deleted_at, total_deposit, updated_at, account_id, id, description, image, title, type) VALUES
                                                                                                                                                                                ('2024-09-30', 0, '2024-09-01', 0, '2024-08-29 13:38:40.865187', NULL, 0, '2024-08-29 14:19:35.100142', 0x01919C6DB3CEB83D89143E25FE34ECA3, 0x01919C6DB363107E1DC67C5B86DC97CD, '지난달보다 커피 소비를 줄이면 성공! 피버타임(오전 7-10시, 오전 11시-오후 2시)에 커피를 마시지 않으면 추가 점수! 건강과 지갑을 동시에 챙기세요.', 'coffee_challenge', '9월 커피 소비 줄이기 챌린지', 'CONSUMPTION_COFFEE'),
                                                                                                                                                                                ('2024-10-19', 0, '2024-09-01', 0, '2024-08-29 13:39:55.918326', NULL, 0, '2024-08-29 14:35:56.818704', 0x01919C6ED8B9840B50D66E4E37A82A9A, 0x01919C6ED8627528C869C2A764114984, '49일 동안 매일 일정 금액을 적금하면 성공! 꾸준한 저축 습관을 만들어보아요.', 'drink_challenge', '777 매일 적금 챌린지', 'SAVINGS_SEVEN'),
                                                                                                                                                                                ('2024-09-14', 0, '2024-08-15', 1, '2024-08-29 13:41:56.578767', NULL, 0, '2024-08-29 14:25:06.379514', 0x01919C70B0576B79B6F6ECA0E5CAAE8D, 0x01919C70AFF4E57A1D9F700E6053E42F, '지난달보다 배달 주문을 줄이면 성공! 특히 피버타임(오후 9시-새벽 2시)에 배달을 자제하면 추가 점수가 부여됩니다! 건강한 식습관을 들여보세요.', 'delivery_challenge', '배달비 절약 챌린지', 'CONSUMPTION_DELIVERY'),
                                                                                                                                                                                ('2024-09-19', 0, '2024-08-20', 1, '2024-08-29 13:43:13.767928', NULL, 0, '2024-08-29 14:26:38.994130', 0x01919C71DDDFF0C092939C6776774211, 0x01919C71DD7F260FAABF4318A51B9BD3, '지난달보다 음주 소비를 줄여보아요! 특히 피버타임(금요일과 토요일)에 술을 마시지 않으면 추가 점수! 나는 알콜프리 근데 취해', 'drink_challenge', '음주 습관 개선 챌린지', 'CONSUMPTION_DRINK'),
                                                                                                                                                                                ('2024-07-31', 0, '2024-07-01', 2, '2024-08-29 13:44:05.055379', NULL, 0, '2024-08-29 14:28:35.108636', 0x01919C72A6348824743639E75691CB4C, 0x01919C72A5CDC78BA7E4E48219D5A64A, '지난달보다 커피 소비를 줄이고, 피버타임(오전 7-10시, 오전 11시-오후 2시)에 커피를 마시지 않으면 추가 점수! 시원한 물은 어떄요?', 'coffee_challenge', '7월 커피 소비 줄이기', 'CONSUMPTION_COFFEE'),
                                                                                                                                                                                ('2024-12-31', 0, '2024-12-01', 0, '2024-08-29 13:45:05.885614', NULL, 0, '2024-08-29 14:29:41.244769', 0x01919C739360D4E0E2F99D1F6C6B42E6, 0x01919C7392C858FFD55A7C25E20DCA49, '매일 금융 퀴즈를 풀고 금융 지식과 혜택을 동시에 얻어가세요! 퀴즈를 많이 맞힐 수록 당첨 확률이 SOL SOL 해져요.', 'quiz_challenge', '12월 금융 퀴즈 챌린지', 'QUIZ_SOLBEING');

INSERT INTO DONG.product (created_at, deleted_at, updated_at, id, account_description, account_name, account_type, account_type_code, account_type_name, account_type_unique_no, bank_code, bank_name) VALUES
                                                                                                                                                                                                           ('2024-08-29 12:51:43.041226', NULL, '2024-08-29 12:51:43.041226', 0x01919C42B4B61E42C585FCC5B2FA3A36, '[관리자] 채널 전용 연동 테스트 계좌', 'ADMIN_CH_PRODUCT', 'DOMESTIC', '1', '입출금통장', '088-1-ee1da36e84f94d', '088', '신한은행'),
                                                                                                                                                                                                           ('2024-08-29 13:14:21.654362', NULL, '2024-08-29 13:14:21.654362', 0x01919C576FD3E9CA6CADD86E40686DFC, 'KB국민은행의 대표 입출금 통장으로, 다양한 혜택과 함께 편리한 금융 생활을 제공합니다.', 'KB 국민 수퍼통장', 'DOMESTIC', '1', '입출금통장', '004-1-14412a66ad834e', '004', '국민은행'),
                                                                                                                                                                                                           ('2024-08-29 13:15:16.661174', NULL, '2024-08-29 13:15:16.661174', 0x01919C5846B1BC2F30FF92D2B2E79DF2, '농협은행의 대표 입출금 통장으로, 전국 어디서나 ATM으로 무료 출금이 가능한 계좌입니다.', 'NH농협 입출금통장', 'DOMESTIC', '1', '입출금통장', '011-1-4870e6b125aa4b', '011', '농협은행'),
                                                                                                                                                                                                           ('2024-08-29 13:15:36.120115', NULL, '2024-08-29 13:15:36.120115', 0x01919C5892B17CD3F9C1CF639481E5E1, '우리은행 대표적인 입출금 통장으로, 다양한 이체 서비스를 무료로 이용할 수 있습니다.', '우리 으뜸 통장', 'DOMESTIC', '1', '입출금통장', '020-1-b588ac674ecf40', '020', '우리은행'),
                                                                                                                                                                                                           ('2024-08-29 13:15:57.806545', NULL, '2024-08-29 13:15:57.806545', 0x01919C58E76AD3EC9341D6863F8B766A, 'KEB하나은행의 대표 입출금 통장으로, 금융 거래 시 다양한 혜택과 우대 서비스를 받을 수 있습니다.', '하나 통합 통장', 'DOMESTIC', '1', '입출금통장', '081-1-b8a97b9ff3c947', '081', 'KEB하나은행'),
                                                                                                                                                                                                           ('2024-08-29 13:16:35.079926', NULL, '2024-08-29 13:16:35.079926', 0x01919C5979058BAEDF325FD3DB648EE0, '신한은행 대표 입출금 통장으로, 다양한 금융 서비스와 편리한 온라인 뱅킹 서비스를 제공합니다.', '신한 주거래 통장', 'DOMESTIC', '1', '입출금통장', '088-1-3209341ffa0b42', '088', '신한은행'),
                                                                                                                                                                                                           ('2024-08-29 13:17:08.879085', NULL, '2024-08-29 13:17:08.879085', 0x01919C59FD0CFFA37E397694E720BF31, '카카오뱅크 대표 계좌로, 모바일 전용 은행, 간편 이체와 저렴한 수수료가 특징인 입출금통장입니다.', '카카오뱅크 입출금통장', 'DOMESTIC', '1', '입출금통장', '090-1-fbf53e02926f4f', '090', '카카오뱅크');



