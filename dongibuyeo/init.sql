DROP
DATABASE IF EXISTS `DONG`;

CREATE
DATABASE `DONG` DEFAULT CHARACTER
SET utf8 COLLATE utf8_unicode_ci;;

USE `DONG`;

CREATE TABLE DONG.`member`
(
    `created_at`    datetime(6)  DEFAULT NULL,
    `deleted_at`    datetime(6)  DEFAULT NULL,
    `name`          varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
    `updated_at`    datetime(6)  DEFAULT NULL,
    `nickname`      varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
    `id`            binary(16) NOT NULL,
    `device_token`  varchar(512) DEFAULT NULL,
    `email`         varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
    `profile_image` varchar(255) DEFAULT NULL,
    `user_key`      varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE DONG.`account`
(
    `created_at`   datetime(6)                  DEFAULT NULL,
    `deleted_at`   datetime(6)                  DEFAULT NULL,
    `updated_at`   datetime(6)                  DEFAULT NULL,
    `id`           binary(16) NOT NULL,
    `member_id`    binary(16)                   DEFAULT NULL,
    `account_no`   varchar(255)                 DEFAULT NULL,
    `account_type` enum ('CHALLENGE','PRIVATE') DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY            `FKr5j0huynd7nsv1s7e9vb8qvwo` (`member_id`),
    CONSTRAINT `FKr5j0huynd7nsv1s7e9vb8qvwo` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE DONG.`challenge`
(
    `end_date`      date                                                                                                   DEFAULT NULL,
    `participants`  int                                                                                                    DEFAULT NULL,
    `start_date`    date                                                                                                   DEFAULT NULL,
    `status`        tinyint                                                                                                DEFAULT NULL,
    `created_at`    datetime(6)                                                                                            DEFAULT NULL,
    `deleted_at`    datetime(6)                                                                                            DEFAULT NULL,
    `total_deposit` bigint                                                                                                 DEFAULT NULL,
    `updated_at`    datetime(6)                                                                                            DEFAULT NULL,
    `account_id`    binary(16)                                                                                             DEFAULT NULL,
    `id`            binary(16) NOT NULL,
    `description`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `image`         varchar(255)                                                                                           DEFAULT NULL,
    `title`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `type`          enum ('CONSUMPTION_COFFEE','CONSUMPTION_DELIVERY','CONSUMPTION_DRINK','QUIZ_SOLBEING','SAVINGS_SEVEN') DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK4saeeac119vkfictd49yb5g67` (`account_id`),
    CONSTRAINT `FKwbq44q4pl5dp1j6q0y5sjg7y` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
    CONSTRAINT `challenge_chk_1` CHECK ((`status` between 0 and 2))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE DONG.`member_challenge`
(
    `is_success`        bit(1)                                              DEFAULT NULL,
    `total_score`       int                                                 DEFAULT NULL,
    `additional_reward` bigint                                              DEFAULT NULL,
    `base_reward`       bigint                                              DEFAULT NULL,
    `created_at`        datetime(6)                                         DEFAULT NULL,
    `deleted_at`        datetime(6)                                         DEFAULT NULL,
    `deposit`           bigint                                              DEFAULT NULL,
    `updated_at`        datetime(6)                                         DEFAULT NULL,
    `challenge_id`      binary(16)                                          DEFAULT NULL,
    `id`                binary(16) NOT NULL,
    `member_id`         binary(16)                                          DEFAULT NULL,
    `status`            enum ('BEFORE_CALCULATION','CALCULATED','REWARDED') DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY                 `FKh9n4f0bidmjun3fvk2jp5netm` (`challenge_id`),
    KEY                 `FK9x810nqdrhsdpp78017y3kqhe` (`member_id`),
    CONSTRAINT `FK9x810nqdrhsdpp78017y3kqhe` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`),
    CONSTRAINT `FKh9n4f0bidmjun3fvk2jp5netm` FOREIGN KEY (`challenge_id`) REFERENCES `challenge` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE DONG.`consumption`
(
    `created_at`                datetime(6)  DEFAULT NULL,
    `deleted_at`                datetime(6)  DEFAULT NULL,
    `transaction_after_balance` bigint       DEFAULT NULL,
    `transaction_balance`       bigint       DEFAULT NULL,
    `updated_at`                datetime(6)  DEFAULT NULL,
    `id`                        binary(16) NOT NULL,
    `member_id`                 binary(16)   DEFAULT NULL,
    `transaction_account_no`    varchar(255) DEFAULT NULL,
    `transaction_date`          varchar(255) DEFAULT NULL,
    `transaction_memo`          varchar(255) DEFAULT NULL,
    `transaction_summary`       varchar(255) DEFAULT NULL,
    `transaction_time`          varchar(255) DEFAULT NULL,
    `transaction_type`          varchar(255) DEFAULT NULL,
    `transaction_type_name`     varchar(255) DEFAULT NULL,
    `transaction_unique_no`     varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY                         `FKra4hpn2ur20o0p56dsvdodj94` (`member_id`),
    CONSTRAINT `FKra4hpn2ur20o0p56dsvdodj94` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE DONG.`daily_score`
(
    `date`                date       DEFAULT NULL,
    `total_score`         int        NOT NULL,
    `id`                  binary(16) NOT NULL,
    `member_challenge_id` binary(16) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY                   `FK36en58l32c8r7ei87cl4gsii3` (`member_challenge_id`),
    CONSTRAINT `FK36en58l32c8r7ei87cl4gsii3` FOREIGN KEY (`member_challenge_id`) REFERENCES `member_challenge` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE DONG.`room`
(
    `created_at` datetime(6)  DEFAULT NULL,
    `deleted_at` datetime(6)  DEFAULT NULL,
    `updated_at` datetime(6)  DEFAULT NULL,
    `id`         binary(16) NOT NULL,
    `name`       varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE DONG.`message`
(
    `created_at` datetime(6)  DEFAULT NULL,
    `deleted_at` datetime(6)  DEFAULT NULL,
    `updated_at` datetime(6)  DEFAULT NULL,
    `id`         binary(16) NOT NULL,
    `member_id`  binary(16)   DEFAULT NULL,
    `room_id`    binary(16)   DEFAULT NULL,
    `message`    varchar(255) DEFAULT NULL,
    `send_at`    varchar(128) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY          `FK4msccpwmxulmw875edu7p352d` (`member_id`),
    KEY          `FKl1kg5a2471cv6pkew0gdgjrmo` (`room_id`),
    CONSTRAINT `FK4msccpwmxulmw875edu7p352d` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`),
    CONSTRAINT `FKl1kg5a2471cv6pkew0gdgjrmo` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE DONG.`product`
(
    `created_at`             datetime(6)  DEFAULT NULL,
    `deleted_at`             datetime(6)  DEFAULT NULL,
    `updated_at`             datetime(6)  DEFAULT NULL,
    `id`                     binary(16) NOT NULL,
    `account_description`    varchar(255) DEFAULT NULL,
    `account_name`           varchar(255) DEFAULT NULL,
    `account_type`           varchar(255) DEFAULT NULL,
    `account_type_code`      varchar(255) DEFAULT NULL,
    `account_type_name`      varchar(255) DEFAULT NULL,
    `account_type_unique_no` varchar(255) DEFAULT NULL,
    `bank_code`              varchar(255) DEFAULT NULL,
    `bank_name`              varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE DONG.`quiz`
(
    `answer`     bit(1)       DEFAULT NULL,
    `created_at` datetime(6)  DEFAULT NULL,
    `deleted_at` datetime(6)  DEFAULT NULL,
    `updated_at` datetime(6)  DEFAULT NULL,
    `id`         binary(16) NOT NULL,
    `question`   varchar(512) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE DONG.`quiz_member`
(
    `created_at` datetime(6) DEFAULT NULL,
    `deleted_at` datetime(6) DEFAULT NULL,
    `solved_at`  datetime(6) DEFAULT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `id`         binary(16) NOT NULL,
    `member_id`  binary(16)  DEFAULT NULL,
    `quiz_id`    binary(16)  DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY          `FK12peccx2dtulmtm31oofjbw61` (`member_id`),
    KEY          `FKe5ckx4p03i3utc2a413uaxahr` (`quiz_id`),
    CONSTRAINT `FK12peccx2dtulmtm31oofjbw61` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`),
    CONSTRAINT `FKe5ckx4p03i3utc2a413uaxahr` FOREIGN KEY (`quiz_id`) REFERENCES `quiz` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE DONG.`score_entries`
(
    `current_total_score` int          DEFAULT NULL,
    `score`               int          DEFAULT NULL,
    `daily_score_id`      binary(16) NOT NULL,
    `description`         varchar(255) DEFAULT NULL,
    KEY                   `FKe23oh1ywc5mgk76qost22vc6q` (`daily_score_id`),
    CONSTRAINT `FKe23oh1ywc5mgk76qost22vc6q` FOREIGN KEY (`daily_score_id`) REFERENCES `daily_score` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO DONG.member (id, email, name, nickname, profile_image, user_key, device_token, created_at, updated_at)
VALUES (UNHEX(REPLACE(UUID(), '-', '')), 'example@example.com', 'John', 'johnny', 'profile.png', NULL,
        'exampleDeviceToken', NOW(), NOW());

INSERT INTO DONG.room (created_at, deleted_at, updated_at, id, name)
VALUES (NOW(), null, NOW(), UNHEX(REPLACE(UUID(), '-', '')), 'COFFEE');

INSERT INTO DONG.room (created_at, deleted_at, updated_at, id, name)
VALUES (NOW(), null, NOW(), UNHEX(REPLACE(UUID(), '-', '')), 'DELIVERY');

INSERT INTO DONG.room (created_at, deleted_at, updated_at, id, name)
VALUES (NOW(), null, NOW(), UNHEX(REPLACE(UUID(), '-', '')), 'DRINK');

INSERT INTO DONG.room (created_at, deleted_at, updated_at, id, name)
VALUES (NOW(), null, NOW(), UNHEX(REPLACE(UUID(), '-', '')), 'SEVEN');

INSERT INTO DONG.room (created_at, deleted_at, updated_at, id, name)
VALUES (NOW(), null, NOW(), UNHEX(REPLACE(UUID(), '-', '')), 'QUIZ');


INSERT INTO DONG.member (created_at, deleted_at, name, updated_at, nickname, id, device_token, email, profile_image,
                         user_key)
VALUES ('2024-08-29 12:51:42', NULL, 'admin', '2024-08-29 12:51:42', 'admin', 0x01919C42B3CEB4F1A612238F5CE70C00, NULL,
        'admin@dongibuyeo-test.com', NULL, '113c59ec-b974-4f04-984f-6642566d4856');
INSERT INTO DONG.member (created_at, deleted_at, name, updated_at, nickname, id, device_token, email, profile_image,
                         user_key)
VALUES ('2024-08-29 12:54:45', NULL, '박수진', '2024-08-29 12:54:45', '박박박', 0x01919C457DCC847749819F669AB5DD46, '',
        'sujin1@naver.com', NULL, '7e72acce-c2a3-4a67-be11-aa9702156257');
INSERT INTO DONG.member (created_at, deleted_at, name, updated_at, nickname, id, device_token, email, profile_image,
                         user_key)
VALUES ('2024-08-29 13:06:37', NULL, '신한', '2024-08-29 14:27:48', '해커톤시렁', 0x01919C505AF68AE77DFFEA6E329C5A6D, '',
        'asd@asd.com', 'Lay', '534effa4-1573-4fb0-a857-aea69ca6568d');
INSERT INTO DONG.member (created_at, deleted_at, name, updated_at, nickname, id, device_token, email, profile_image,
                         user_key)
VALUES ('2024-08-29 13:23:25', NULL, '김신한', '2024-08-29 13:23:25', 'shinhanKim', 0x01919C5FBAC69C61BAD76CE5F992440E, '',
        'shinhanKim@dongibuyeo-test.com', '', '5971b18f-bfd6-49a4-b1c8-883d70ebb803');
INSERT INTO DONG.member (created_at, deleted_at, name, updated_at, nickname, id, device_token, email, profile_image,
                         user_key)
VALUES ('2024-08-29 14:53:56', NULL, '곽곽', '2024-08-29 14:53:56', '곽곽이', 0x01919CB29B1033C9DCF879E53C3B31F2, '',
        'test123@test.co', NULL, 'cbe35d3c-caed-42ca-8caf-eaa25081ee94');

INSERT INTO DONG.account (created_at, deleted_at, updated_at, id, member_id, account_no, account_type)
VALUES
    ('2024-08-29 13:24:02', NULL, '2024-08-29 13:24:02.964314', 0x01919C604E86334E71442B0E6AA8B8AE,
     0x01919C5FBAC69C61BAD76CE5F992440E, '0042401830256381', 'PRIVATE'),
    ('2024-08-29 13:24:35', NULL, '2024-08-29 13:24:35.810119', 0x01919C60CED7C389FB1E29F132E6A5FA,
     0x01919C5FBAC69C61BAD76CE5F992440E, '0111427268290839', 'PRIVATE'),
    ('2024-08-29 13:25:02', NULL, '2024-08-29 13:25:02.677679', 0x01919C6137CFF4AAE65FADA7BF87524C,
     0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', 'PRIVATE'),
    ('2024-08-29 13:26:21', NULL, '2024-08-29 13:26:21.064392', 0x01919C626A023FB66374ACE2C02365A6,
     0x01919C5FBAC69C61BAD76CE5F992440E, '0881763003647857', 'PRIVATE'),
    ('2024-08-29 13:31:18', NULL, '2024-08-29 13:31:18.086350', 0x01919C66F23F4D7949CF037FA9AACCD9,
     0x01919C5FBAC69C61BAD76CE5F992440E, '0887556547087119', 'CHALLENGE'),
    ('2024-08-29 13:38:40', NULL, '2024-08-29 13:38:40.866248', 0x01919C6DB3CEB83D89143E25FE34ECA3,
     0x01919C42B3CEB4F1A612238F5CE70C00, '0889713108612288', 'CHALLENGE'),
    ('2024-08-29 13:39:55', NULL, '2024-08-29 13:39:55.918940', 0x01919C6ED8B9840B50D66E4E37A82A9A,
     0x01919C42B3CEB4F1A612238F5CE70C00, '0887447183036205', 'CHALLENGE'),
    ('2024-08-29 13:41:56', NULL, '2024-08-29 13:41:56.579747', 0x01919C70B0576B79B6F6ECA0E5CAAE8D,
     0x01919C42B3CEB4F1A612238F5CE70C00, '0888396451871590', 'CHALLENGE'),
    ('2024-08-29 13:43:13', NULL, '2024-08-29 13:43:13.768534', 0x01919C71DDDFF0C092939C6776774211,
     0x01919C42B3CEB4F1A612238F5CE70C00, '0885898830115588', 'CHALLENGE'),
    ('2024-08-29 13:44:05', NULL, '2024-08-29 13:44:05.056436', 0x01919C72A6348824743639E75691CB4C,
     0x01919C42B3CEB4F1A612238F5CE70C00, '0886860797262912', 'CHALLENGE'),
    ('2024-08-29 13:45:05', NULL, '2024-08-29 13:45:05.886824', 0x01919C739360D4E0E2F99D1F6C6B42E6,
     0x01919C42B3CEB4F1A612238F5CE70C00, '0886377511023806', 'CHALLENGE'),
    ('2024-08-29 13:52:09', NULL, '2024-08-29 13:52:09.536775', 0x01919C7A0AB82D56D98ED81CDC14EA3F,
     0x01919C5FBAC69C61BAD76CE5F992440E, '0883038929', 'PRIVATE'),
    ('2024-08-29 13:53:43', NULL, '2024-08-29 13:53:43.542246', 0x01919C7B79ED9437CA6870707E44C8F1,
     0x01919C5FBAC69C61BAD76CE5F992440E, '0012954192', 'PRIVATE'),
    ('2024-08-29 13:53:43', NULL, '2024-08-29 13:53:43.542246', 0x01919F1D332D9FBB83D41F7B4C58656E,
     0x01919C5FBAC69C61BAD76CE5F992440E, '0880602950438998', 'CHALLENGE'),
    ('2024-08-30 00:00:00', NULL, '2024-08-30 00:00:00', UNHEX(REPLACE(UUID(), '-', '')),
     0x01919C5FBAC69C61BAD76CE5F992440E, '0886446161', 'CHALLENGE');


INSERT INTO DONG.challenge (end_date, participants, start_date, status, created_at, deleted_at, total_deposit,
                            updated_at, account_id, id, description, image, title, type)
VALUES ('2024-09-30', 0, '2024-09-01', 0, '2024-08-29 13:38:40.865187', NULL, 0, '2024-08-29 14:19:35.100142',
        0x01919C6DB3CEB83D89143E25FE34ECA3, 0x01919C6DB363107E1DC67C5B86DC97CD,
        '지난달보다 커피 소비를 줄이면 성공! 피버타임(오전 7-10시, 오전 11시-오후 2시)에 커피를 마시지 않으면 추가 점수! 건강과 지갑을 동시에 챙기세요.', 'coffee_challenge',
        '9월 커피 소비 줄이기 챌린지', 'CONSUMPTION_COFFEE'),
       ('2024-10-19', 0, '2024-09-01', 0, '2024-08-29 13:39:55.918326', NULL, 0, '2024-08-29 14:35:56.818704',
        0x01919C6ED8B9840B50D66E4E37A82A9A, 0x01919C6ED8627528C869C2A764114984,
        '49일 동안 매일 일정 금액을 적금하면 성공! 꾸준한 저축 습관을 만들어보아요.', 'savings_challenge', '777 매일 적금 챌린지', 'SAVINGS_SEVEN'),
       ('2024-09-14', 0, '2024-08-15', 1, '2024-08-29 13:41:56.578767', NULL, 0, '2024-08-29 14:25:06.379514',
        0x01919C70B0576B79B6F6ECA0E5CAAE8D, 0x01919C70AFF4E57A1D9F700E6053E42F,
        '지난달보다 배달 주문을 줄이면 성공! 특히 피버타임(오후 9시-새벽 2시)에 배달을 자제하면 추가 점수가 부여됩니다! 건강한 식습관을 들여보세요.', 'delivery_challenge',
        '배달비 절약 챌린지', 'CONSUMPTION_DELIVERY'),
       ('2024-09-19', 0, '2024-08-20', 1, '2024-08-29 13:43:13.767928', NULL, 0, '2024-08-29 14:26:38.994130',
        0x01919C71DDDFF0C092939C6776774211, 0x01919C71DD7F260FAABF4318A51B9BD3,
        '지난달보다 음주 소비를 줄여보아요! 특히 피버타임(금요일과 토요일)에 술을 마시지 않으면 추가 점수! 나는 알콜프리 근데 취해', 'drink_challenge', '음주 습관 개선 챌린지',
        'CONSUMPTION_DRINK'),
       ('2024-07-31', 0, '2024-07-01', 2, '2024-08-29 13:44:05.055379', NULL, 0, '2024-08-29 14:28:35.108636',
        0x01919C72A6348824743639E75691CB4C, 0x01919C72A5CDC78BA7E4E48219D5A64A,
        '지난달보다 커피 소비를 줄이고, 피버타임(오전 7-10시, 오전 11시-오후 2시)에 커피를 마시지 않으면 추가 점수! 시원한 물은 어떄요?', 'coffee_challenge',
        '7월 커피 소비 줄이기', 'CONSUMPTION_COFFEE'),
       ('2024-12-31', 0, '2024-12-01', 0, '2024-08-29 13:45:05.885614', NULL, 0, '2024-08-29 14:29:41.244769',
        0x01919C739360D4E0E2F99D1F6C6B42E6, 0x01919C7392C858FFD55A7C25E20DCA49,
        '매일 금융 퀴즈를 풀고 금융 지식과 혜택을 동시에 얻어가세요! 퀴즈를 많이 맞힐 수록 당첨 확률이 SOL SOL 해져요.', 'quiz_challenge', '12월 금융 퀴즈 챌린지',
        'QUIZ_SOLBEING'),
       ('2024-05-31', 1, '2024-04-01', 2, '2024-08-29 13:41:56.578767', NULL, 49000, '2024-08-29 13:41:56.578767',
        0x01919F1D332D9FBB83D41F7B4C58656E, 0x01919F1D332D9FBB83D41F7B4C58656F,
        '49일 동안 매일 일정 금액을 적금하면 성공! 꾸준한 저축 습관을 만들어보아요.', 'savings_challenge', '777 매일 적금 챌린지', 'SAVINGS_SEVEN');
;

INSERT INTO DONG.product (created_at, deleted_at, updated_at, id, account_description, account_name, account_type,
                          account_type_code, account_type_name, account_type_unique_no, bank_code, bank_name)
VALUES ('2024-08-29 12:51:43.041226', NULL, '2024-08-29 12:51:43.041226', 0x01919C42B4B61E42C585FCC5B2FA3A36,
        '[관리자] 채널 전용 연동 테스트 계좌', 'ADMIN_CH_PRODUCT', 'DOMESTIC', '1', '입출금통장', '088-1-ee1da36e84f94d', '088', '신한은행'),
       ('2024-08-29 13:14:21.654362', NULL, '2024-08-29 13:14:21.654362', 0x01919C576FD3E9CA6CADD86E40686DFC,
        'KB국민은행의 대표 입출금 통장으로, 다양한 혜택과 함께 편리한 금융 생활을 제공합니다.', 'KB 국민 수퍼통장', 'DOMESTIC', '1', '입출금통장',
        '004-1-14412a66ad834e', '004', '국민은행'),
       ('2024-08-29 13:15:16.661174', NULL, '2024-08-29 13:15:16.661174', 0x01919C5846B1BC2F30FF92D2B2E79DF2,
        '농협은행의 대표 입출금 통장으로, 전국 어디서나 ATM으로 무료 출금이 가능한 계좌입니다.', 'NH농협 입출금통장', 'DOMESTIC', '1', '입출금통장',
        '011-1-4870e6b125aa4b', '011', '농협은행'),
       ('2024-08-29 13:15:36.120115', NULL, '2024-08-29 13:15:36.120115', 0x01919C5892B17CD3F9C1CF639481E5E1,
        '우리은행 대표적인 입출금 통장으로, 다양한 이체 서비스를 무료로 이용할 수 있습니다.', '우리 으뜸 통장', 'DOMESTIC', '1', '입출금통장', '020-1-b588ac674ecf40',
        '020', '우리은행'),
       ('2024-08-29 13:15:57.806545', NULL, '2024-08-29 13:15:57.806545', 0x01919C58E76AD3EC9341D6863F8B766A,
        'KEB하나은행의 대표 입출금 통장으로, 금융 거래 시 다양한 혜택과 우대 서비스를 받을 수 있습니다.', '하나 통합 통장', 'DOMESTIC', '1', '입출금통장',
        '081-1-b8a97b9ff3c947', '081', 'KEB하나은행'),
       ('2024-08-29 13:16:35.079926', NULL, '2024-08-29 13:16:35.079926', 0x01919C5979058BAEDF325FD3DB648EE0,
        '신한은행 대표 입출금 통장으로, 다양한 금융 서비스와 편리한 온라인 뱅킹 서비스를 제공합니다.', '신한 주거래 통장', 'DOMESTIC', '1', '입출금통장',
        '088-1-3209341ffa0b42', '088', '신한은행'),
       ('2024-08-29 13:17:08.879085', NULL, '2024-08-29 13:17:08.879085', 0x01919C59FD0CFFA37E397694E720BF31,
        '카카오뱅크 대표 계좌로, 모바일 전용 은행, 간편 이체와 저렴한 수수료가 특징인 입출금통장입니다.', '카카오뱅크 입출금통장', 'DOMESTIC', '1', '입출금통장',
        '090-1-fbf53e02926f4f', '090', '카카오뱅크');


INSERT INTO DONG.member_challenge (is_success, total_score, additional_reward, base_reward, created_at, deleted_at,
                                   deposit, updated_at, challenge_id, id, member_id, status)
VALUES (0x00, 0, 0, 0, '2024-08-29 23:59:59', NULL, 10000, '2024-08-30 00:00:00',
        0x01919C6DB363107E1DC67C5B86DC97CD, 0x01919EB22B3181389DB1D191EB0880BB, 0x01919C5FBAC69C61BAD76CE5F992440E,
        'BEFORE_CALCULATION'), -- 9월 커피 소비 줄이기 챌린지 (예정됨)
       (0x00, 0, 0, 0, '2024-08-29 23:50:00', NULL, 10000, '2024-08-30 00:05:00',
        0x01919C6ED8627528C869C2A764114984, 0x01919EB57A421D6698B969D27809E3A0, 0x01919C5FBAC69C61BAD76CE5F992440E,
        'BEFORE_CALCULATION'), -- 777 매일 적금 챌린지 (예정됨)
       (0x00, 90, 0, 0, '2024-08-29 23:45:00', NULL, 10000, '2024-08-29 14:00:00',
        0x01919C70AFF4E57A1D9F700E6053E42F, 0x01919C70B0576B79B6F6ECA0E5CAAE8D, 0x01919C5FBAC69C61BAD76CE5F992440E,
        'BEFORE_CALCULATION'), -- 배달비 절약 챌린지 (진행 중)
       (0x00, 70, 0, 0, '2024-08-29 23:40:00', NULL, 10000, '2024-08-29 14:05:00',
        0x01919C71DD7F260FAABF4318A51B9BD3, 0x01919C71DDDFF0C092939C6776774211, 0x01919C5FBAC69C61BAD76CE5F992440E,
        'BEFORE_CALCULATION'), -- 음주 습관 개선 챌린지 (진행 중)
       (0x01, 85, 400, 10000, '2024-07-30 23:59:00', NULL, 10000, '2024-07-30 23:59:00',
        0x01919C72A5CDC78BA7E4E48219D5A64A, 0x01919C72A6348824743639E75691CB4C, 0x01919C5FBAC69C61BAD76CE5F992440E,
        'REWARDED'),           -- 7월 커피 소비 줄이기 (완료됨)
       (0x00, 0, 0, 0, '2024-08-29 23:55:00', NULL, 10000, '2024-08-29 14:10:00',
        0x01919C7392C858FFD55A7C25E20DCA49, 0x01919C739360D4E0E2F99D1F6C6B42E6, 0x01919C5FBAC69C61BAD76CE5F992440E,
        'BEFORE_CALCULATION'),
       (0x01, 305, 1000, 50000, '2024-03-29 23:55:00', NULL, 70000, '2024-08-29 23:55:00',
        0x01919F1D332D9FBB83D41F7B4C58656F, 0x01919F1D332D9FBB83D41F7B4C586570, 0x01919C5FBAC69C61BAD76CE5F992440E,
        'CALCULATED');

UPDATE DONG.member_challenge
SET is_success = FALSE, base_reward = 0, additional_reward = 0, total_score = 0
WHERE status = 'BEFORE_CALCULATION';

-- DailyScore 데이터 생성 (IN_PROGRESS 및 COMPLETED 챌린지만)
INSERT INTO DONG.daily_score (id, member_challenge_id, date, total_score)
SELECT
    UNHEX(MD5(CONCAT(mc.id, DATE_ADD(c.start_date, INTERVAL seq.seq DAY)))),
    mc.id,
    DATE_ADD(c.start_date, INTERVAL seq.seq DAY),
    0  -- 초기 총점은 0으로 설정
FROM
    DONG.member_challenge mc
        JOIN DONG.challenge c ON mc.challenge_id = c.id
        JOIN (
        SELECT a.N + b.N * 10 + c.N * 100 AS seq
        FROM (SELECT 0 AS N UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) a
                 CROSS JOIN (SELECT 0 AS N UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) b
                 CROSS JOIN (SELECT 0 AS N UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) c
    ) seq
WHERE
    c.status IN (1, 2)  -- IN_PROGRESS 또는 COMPLETED 상태의 챌린지만
  AND mc.status IN ('BEFORE_CALCULATION', 'CALCULATED', 'REWARDED')
  AND DATE_ADD(c.start_date, INTERVAL seq.seq DAY) <= LEAST(c.end_date, CURDATE());

-- DAILY_SCORE 생성
INSERT INTO DONG.score_entries (daily_score_id, description, score, current_total_score)
SELECT
    ds.id,
    'DAILY_SCORE',
    10,
    0
FROM
    DONG.daily_score ds;

-- 챌린지 타입별 추가 점수 생성
INSERT INTO DONG.score_entries (daily_score_id, description, score, current_total_score)
SELECT
    ds.id,
    CASE
        WHEN c.type = 'CONSUMPTION_COFFEE' THEN
            CASE
                WHEN HOUR(ds.date) BETWEEN 7 AND 9 THEN '[FEVER] 7AM-10AM'
                WHEN HOUR(ds.date) BETWEEN 11 AND 13 THEN '[FEVER] 11AM-2PM'
                ELSE 'CONSUMPTION_COFFEE'
END
WHEN c.type = 'CONSUMPTION_DELIVERY' THEN
            CASE
                WHEN HOUR(ds.date) BETWEEN 21 AND 23 OR HOUR(ds.date) < 2 THEN '[FEVER] 9PM-2AM'
                ELSE 'CONSUMPTION_DELIVERY'
END
WHEN c.type = 'CONSUMPTION_DRINK' THEN
            CASE
                WHEN DAYOFWEEK(ds.date) IN (6, 7) THEN '[FEVER] Weekend'
                ELSE 'CONSUMPTION_DRINK'
END
WHEN c.type = 'QUIZ_SOLBEING' THEN 'SOLVE_QUIZ'
        WHEN c.type = 'SAVINGS_SEVEN' THEN 'DAILY SAVINGS'
END AS description,
    CASE
        WHEN c.type = 'CONSUMPTION_COFFEE' THEN
            CASE
                WHEN HOUR(ds.date) BETWEEN 7 AND 9 THEN 2
                WHEN HOUR(ds.date) BETWEEN 11 AND 13 THEN 3
                ELSE -5
END
WHEN c.type = 'CONSUMPTION_DELIVERY' THEN
            CASE
                WHEN HOUR(ds.date) BETWEEN 21 AND 23 OR HOUR(ds.date) < 2 THEN 5
                ELSE -5
END
WHEN c.type = 'CONSUMPTION_DRINK' THEN
            CASE
                WHEN DAYOFWEEK(ds.date) IN (6, 7) THEN 5
                ELSE -5
END
WHEN c.type = 'QUIZ_SOLBEING' THEN 5
        WHEN c.type = 'SAVINGS_SEVEN' THEN 5
END AS score,
    0 AS current_total_score
FROM
    DONG.daily_score ds
        JOIN DONG.member_challenge mc ON ds.member_challenge_id = mc.id
        JOIN DONG.challenge c ON mc.challenge_id = c.id
WHERE
    CASE
        WHEN c.type IN ('CONSUMPTION_COFFEE', 'CONSUMPTION_DELIVERY', 'CONSUMPTION_DRINK') THEN RAND() < 0.7
        WHEN c.type = 'QUIZ_SOLBEING' THEN RAND() < 0.8
        WHEN c.type = 'SAVINGS_SEVEN' THEN RAND() < 0.95
        ELSE FALSE
END;

-- current_total_score 업데이트 (챌린지 시작부터 현재 항목까지의 누적 점수)
UPDATE DONG.score_entries se
    JOIN (
    SELECT
    se2.daily_score_id,
    se2.description,
    @running_total := @running_total + se2.score AS current_total_score
    FROM
    (SELECT * FROM DONG.score_entries
    JOIN DONG.daily_score ON DONG.score_entries.daily_score_id = DONG.daily_score.id
    ORDER BY DONG.daily_score.member_challenge_id, DONG.daily_score.date, DONG.score_entries.description) se2,
    (SELECT @running_total := 0) vars
    ) AS calc
SET se.current_total_score = calc.current_total_score
WHERE se.daily_score_id = calc.daily_score_id AND se.description = calc.description;

-- DailyScore의 total_score 업데이트
UPDATE DONG.daily_score ds
    JOIN (
    SELECT daily_score_id, MAX(current_total_score) AS max_score
    FROM DONG.score_entries
    GROUP BY daily_score_id
    ) se ON ds.id = se.daily_score_id
    SET ds.total_score = se.max_score
WHERE ds.total_score != se.max_score OR ds.total_score IS NULL;

-- MemberChallenge의 total_score 업데이트
UPDATE DONG.member_challenge mc
    JOIN (
    SELECT member_challenge_id, MAX(total_score) AS max_total_score
    FROM DONG.daily_score
    GROUP BY member_challenge_id
    ) ds ON mc.id = ds.member_challenge_id
    SET mc.total_score = ds.max_total_score
WHERE mc.total_score != ds.max_total_score OR mc.total_score IS NULL;

-- Challenge의 participants와 total_deposit 업데이트
UPDATE DONG.challenge c
    JOIN (
    SELECT
    challenge_id,
    COUNT(DISTINCT id) AS participant_count,
    COALESCE(SUM(deposit), 0) AS total_deposit_sum
    FROM DONG.member_challenge
    GROUP BY challenge_id
    ) mc ON c.id = mc.challenge_id
    SET
        c.participants = mc.participant_count,
        c.total_deposit = mc.total_deposit_sum
WHERE
    c.status IN (1, 2)  -- IN_PROGRESS 또는 COMPLETED 상태의 챌린지만 업데이트
  AND (c.participants != mc.participant_count
   OR c.total_deposit != mc.total_deposit_sum
   OR c.participants IS NULL
   OR c.total_deposit IS NULL);

INSERT INTO quiz (id, question, answer) VALUES
                                            (UNHEX(REPLACE(UUID(), '-', '')), '부채는 기업의 자산에서 빚을 뺀 금액을 의미한다.', FALSE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '주식 시장에서 "불장"은 주가가 상승하는 시기를 의미한다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), 'CD (Certificate of Deposit)는 양도성 예금증서를 의미한다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '금리는 돈의 시간적 가치와 관련된 개념이다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '유동성 위험은 자산을 현금화하는데 시간이 오래 걸릴 위험을 의미한다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '주가는 기업의 실적과 상관없이 항상 일정하다.', FALSE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '채권은 고정된 이자율로 일정 기간 동안 이자를 지급하는 금융 상품이다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '펀드는 개인이 직접 주식을 구매하지 않고, 전문가가 운용하는 자산에 투자하는 방법이다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '주식 분할은 주주의 지분 비율을 줄이는 행위이다.', FALSE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '부동산은 금융 자산에 포함된다.', FALSE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '복리 이자는 단리 이자보다 더 많이 적립된다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '인플레이션은 물가가 상승하고 화폐 가치가 떨어지는 현상을 의미한다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '디플레이션은 경제가 과열되었을 때 발생하는 현상이다.', FALSE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '달러 가치가 상승하면 원/달러 환율이 하락한다.', FALSE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '주식의 시가총액은 주가와 발행 주식 수를 곱한 값이다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '예금자 보호 제도는 은행이 파산하더라도 일정 금액까지 예금을 보호해주는 제도이다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '채권의 가격이 오르면 채권의 수익률도 상승한다.', FALSE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '국채는 정부가 발행하는 채권으로, 안전 자산으로 간주된다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '변동 금리 상품은 고정 금리 상품보다 예측 가능성이 높다.', FALSE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '신용 점수가 낮을수록 대출 금리는 더 높아진다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '주식은 기업의 소유권을 나타낸다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '인덱스 펀드는 특정 지수의 성과를 따라가는 것을 목표로 한다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), 'ETF는 상장지수펀드를 의미한다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '파생상품은 기초자산의 가치 변동에 따라 가격이 변동되는 금융 상품이다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '비트코인은 중앙은행에 의해 발행된다.', FALSE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '매출액이 클수록 순이익도 항상 크다.', FALSE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '경제 성장률이 높아지면 물가 상승률도 항상 높아진다.', FALSE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '리스크와 수익률은 대체로 반비례 관계에 있다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '기업이 채권을 발행할 때는 반드시 담보가 필요하다.', FALSE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '채권의 신용 등급이 높을수록 수익률이 낮을 가능성이 크다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '무상증자는 주주에게 새로운 주식을 무료로 배분하는 것이다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '투자자들이 주식을 매수할 때 사용하는 전략 중 하나가 "바이 앤 홀드" 전략이다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '적립식 펀드는 일정 금액을 정기적으로 투자하는 방식이다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '회사의 자본금은 주식 발행을 통해 조달된다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '주식을 공모하면 일반 대중에게 주식을 발행하는 것을 의미한다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '채권의 만기가 길수록 이자율의 변동에 더 민감하다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '개인연금은 퇴직 후 노후 생활을 위해 마련하는 자금이다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '주주총회는 기업의 주요 의사결정을 위해 열리는 회의이다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '국내 총생산(GDP)은 한 나라에서 일정 기간 동안 생산된 모든 재화와 서비스의 시장 가치 총액을 의미한다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '펀드 매니저는 투자 포트폴리오를 관리하는 전문가이다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '실질 금리는 명목 금리에서 인플레이션율을 뺀 금리이다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '장기적으로 물가가 하락하는 현상을 디플레이션이라고 한다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '일반적으로 경기 불황 시에는 금리가 상승한다.', FALSE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '신용카드 사용 시 이자율은 항상 고정되어 있다.', FALSE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '경제 성장률이 높아지면 실업률도 항상 낮아진다.', FALSE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '환율이 하락하면 수출이 증가할 가능성이 높다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '달러화 강세는 미국의 수출 경쟁력을 높인다.', FALSE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '금리가 인상되면 대출 수요는 감소할 가능성이 크다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '주식시장에서 "베어 마켓"은 주가가 하락하는 시장을 의미한다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '배당 수익률은 주가 대비 배당금의 비율을 의미한다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '채권의 액면가는 만기 시 받을 수 있는 금액이다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '주식은 배당을 받을 수 있는 권리를 제공한다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '회사의 부채비율은 총 부채를 총 자산으로 나눈 값이다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '리츠(REITs)는 부동산 투자신탁을 의미한다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '경상수지는 한 나라의 수출과 수입 간의 차이를 나타낸다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '기준 금리는 중앙은행이 금융 기관 간 거래에 적용하는 금리를 의미한다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '유로화는 유럽 연합 회원국 전체에서 통용되는 단일 통화이다.', FALSE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '분산 투자 전략은 하나의 자산에만 집중 투자하는 것이다.', FALSE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '주식시장에서 "상한가"는 하루 동안 주가가 상승할 수 있는 최대치를 의미한다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '보통주 주주는 회사 청산 시 우선주 주주보다 먼저 배당을 받는다.', FALSE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '금리가 인하되면 일반적으로 주식시장은 긍정적인 반응을 보인다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '자본이득세는 자산의 매매 차익에 대해 부과되는 세금이다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '펀드의 순자산 가치는 펀드가 보유한 자산의 가치에서 부채를 뺀 값이다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '공급이 증가하면 일반적으로 가격이 상승한다.', FALSE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '주식에서 PER(Price to Earnings Ratio)은 주가를 주당순이익으로 나눈 값을 의미한다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '법정통화는 정부가 강제로 가치를 인정하는 화폐를 의미한다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '외환보유액은 한 나라가 보유한 외화 및 금의 총액을 의미한다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '기업의 자기자본이익률(ROE)은 순이익을 자기자본으로 나눈 비율이다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '금리 상승은 통화가치의 하락을 초래한다.', FALSE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '주주총회에서 의결권이 없는 주식을 우선주라고 한다.', FALSE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '상장지수펀드(ETF)는 주식과 유사하게 거래소에서 거래된다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '통화정책은 중앙은행이 물가 안정과 경제 성장을 위해 금리와 통화량을 조절하는 정책이다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '금본위제는 금의 가치에 기반하여 통화를 발행하는 시스템이다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '주가가 높아지면 PER(주가수익비율)도 항상 높아진다.', FALSE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '디플레이션은 경제 활동이 둔화되고 물가가 지속적으로 하락하는 현상이다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '부동산은 변동성이 낮고 유동성이 높은 자산으로 간주된다.', FALSE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '무위험 수익률은 투자자가 위험 없이 얻을 수 있는 최소한의 수익률이다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '적립식 펀드는 일정 금액을 정기적으로 투자하는 방식이다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '국제통화기금(IMF)은 국제적인 통화 안정을 위해 설립된 기구이다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '중앙은행이 금리를 인상하면 일반적으로 대출 수요가 증가한다.', FALSE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '경상수지는 한 나라의 수출과 수입 간의 차이를 나타낸다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '파산한 기업의 주주는 자산을 먼저 청구할 권리가 있다.', FALSE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '채권의 신용등급이 높을수록 수익률이 낮을 가능성이 크다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '배당은 기업이 이익을 주주에게 분배하는 방법 중 하나이다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '펀드는 투자자들이 위험을 분산하기 위해 다양한 자산에 투자하는 금융 상품이다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '고정금리 대출은 대출 기간 동안 이자율이 변하지 않는 대출이다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '개인 신용 등급은 금융 기관에서 대출 심사를 할 때 중요한 요소로 고려된다.', TRUE),
                                            (UNHEX(REPLACE(UUID(), '-', '')), '물가가 상승할 때, 명목 금리와 실질 금리는 동일하게 상승한다.', FALSE);

-- SQL Insert statements for the consumption table with unique UUIDs and transaction_unique_no

INSERT INTO DONG.`consumption` (
    created_at, updated_at, deleted_at,
    transaction_after_balance, transaction_balance,
    id, member_id, transaction_account_no,
    transaction_date, transaction_memo, transaction_summary,
    transaction_time, transaction_type, transaction_type_name,
    transaction_unique_no
) VALUES
-- Record 1
(NOW(), NOW(), NULL, 100000, 95000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240301', 'Coffee purchase', 'COFFEE출금',
 '103220', '2', '출금(이체)', '1001'),
-- Record 2
(NOW(), NOW(), NULL, 105000, 100000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240415', 'Drink purchase', 'DRINK출금',
 '121015', '2', '출금(이체)', '1002'),
-- Record 3
(NOW(), NOW(), NULL, 200000, 190000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240520', 'Delivery service', 'DELIVERY출금',
 '140320', '2', '출금(이체)', '1003'),
-- Record 4
(NOW(), NOW(), NULL, 250000, 240000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240605', 'Coffee purchase', 'COFFEE출금',
 '153045', '2', '출금(이체)', '1004'),
-- Record 5
(NOW(), NOW(), NULL, 300000, 290000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240710', 'Coffee purchase', 'COFFEE출금',
 '162530', '2', '출금(이체)', '1005'),
-- Record 6
(NOW(), NOW(), NULL, 320000, 310000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240801', 'Drink purchase', 'DRINK출금',
 '174500', '2', '출금(이체)', '1006'),
-- Record 7
(NOW(), NOW(), NULL, 150000, 140000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240315', 'Delivery service', 'DELIVERY출금',
 '110000', '2', '출금(이체)', '1007'),
-- Record 8
(NOW(), NOW(), NULL, 170000, 160000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240420', 'Coffee purchase', 'COFFEE출금',
 '121530', '2', '출금(이체)', '1008'),
-- Record 9
(NOW(), NOW(), NULL, 180000, 170000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240525', 'Drink purchase', 'DRINK출금',
 '130000', '2', '출금(이체)', '1009'),
-- Record 10
(NOW(), NOW(), NULL, 190000, 180000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240610', 'Delivery service', 'DELIVERY출금',
 '140000', '2', '출금(이체)', '1010'),
-- Record 11
(NOW(), NOW(), NULL, 200000, 190000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240715', 'Coffee purchase', 'COFFEE출금',
 '150000', '2', '출금(이체)', '1011'),
-- Record 12
(NOW(), NOW(), NULL, 210000, 200000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240805', 'Drink purchase', 'DRINK출금',
 '160000', '2', '출금(이체)', '1012'),
-- Record 13
(NOW(), NOW(), NULL, 220000, 210000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240320', 'Delivery service', 'DELIVERY출금',
 '170000', '2', '출금(이체)', '1013'),
-- Record 14
(NOW(), NOW(), NULL, 230000, 220000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240410', 'Coffee purchase', 'COFFEE출금',
 '180000', '2', '출금(이체)', '1014'),
-- Record 15
(NOW(), NOW(), NULL, 240000, 230000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240515', 'Drink purchase', 'DRINK출금',
 '190000', '2', '출금(이체)', '1015'),
-- Record 16
(NOW(), NOW(), NULL, 250000, 240000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240620', 'Delivery service', 'DELIVERY출금',
 '200000', '2', '출금(이체)', '1016'),
-- Record 17
(NOW(), NOW(), NULL, 260000, 250000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240725', 'Coffee purchase', 'COFFEE출금',
 '210000', '2', '출금(이체)', '1017'),
-- Record 18
(NOW(), NOW(), NULL, 270000, 260000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240810', 'Drink purchase', 'DRINK출금',
 '220000', '2', '출금(이체)', '1018'),
-- Record 19
(NOW(), NOW(), NULL, 280000, 270000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240305', 'Delivery service', 'DELIVERY출금',
 '231500', '2', '출금(이체)', '1019'),
-- Record 20
(NOW(), NOW(), NULL, 290000, 280000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240430', 'Coffee purchase', 'COFFEE출금',
 '244500', '2', '출금(이체)', '1020'),
-- Record 21
(NOW(), NOW(), NULL, 300000, 290000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240510', 'Drink purchase', 'DRINK출금',
 '253000', '2', '출금(이체)', '1021'),
-- Record 22
(NOW(), NOW(), NULL, 310000, 300000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240601', 'Delivery service', 'DELIVERY출금',
 '263000', '2', '출금(이체)', '1022'),
-- Record 23
(NOW(), NOW(), NULL, 320000, 310000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240705', 'Coffee purchase', 'COFFEE출금',
 '273000', '2', '출금(이체)', '1023'),
-- Record 24
(NOW(), NOW(), NULL, 330000, 320000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240801', 'Drink purchase', 'DRINK출금',
 '283000', '2', '출금(이체)', '1024'),
-- Record 25
(NOW(), NOW(), NULL, 340000, 330000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240310', 'Delivery service', 'DELIVERY출금',
 '293000', '2', '출금(이체)', '1025'),
-- Record 26
(NOW(), NOW(), NULL, 350000, 340000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240420', 'Coffee purchase', 'COFFEE출금',
 '303000', '2', '출금(이체)', '1026'),
-- Record 27
(NOW(), NOW(), NULL, 360000, 350000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240515', 'Drink purchase', 'DRINK출금',
 '313000', '2', '출금(이체)', '1027'),
-- Record 28
(NOW(), NOW(), NULL, 370000, 360000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240625', 'Delivery service', 'DELIVERY출금',
 '323000', '2', '출금(이체)', '1028'),
-- Record 29
(NOW(), NOW(), NULL, 380000, 370000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240710', 'Coffee purchase', 'COFFEE출금',
 '333000', '2', '출금(이체)', '1029'),
-- Record 30
(NOW(), NOW(), NULL, 390000, 380000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240801', 'Drink purchase', 'DRINK출금',
 '343000', '2', '출금(이체)', '1030'),
-- Record 31
(NOW(), NOW(), NULL, 400000, 390000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240325', 'Delivery service', 'DELIVERY출금',
 '353000', '2', '출금(이체)', '1031'),
-- Record 32
(NOW(), NOW(), NULL, 410000, 400000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240430', 'Coffee purchase', 'COFFEE출금',
 '363000', '2', '출금(이체)', '1032'),
-- Record 33
(NOW(), NOW(), NULL, 420000, 410000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240515', 'Drink purchase', 'DRINK출금',
 '373000', '2', '출금(이체)', '1033'),
-- Record 34
(NOW(), NOW(), NULL, 430000, 420000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240610', 'Delivery service', 'DELIVERY출금',
 '383000', '2', '출금(이체)', '1034'),
-- Record 35
(NOW(), NOW(), NULL, 440000, 430000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240720', 'Coffee purchase', 'COFFEE출금',
 '393000', '2', '출금(이체)', '1035'),
-- Record 36
(NOW(), NOW(), NULL, 450000, 440000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240805', 'Drink purchase', 'DRINK출금',
 '403000', '2', '출금(이체)', '1036'),
-- Record 37
(NOW(), NOW(), NULL, 460000, 450000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240330', 'Delivery service', 'DELIVERY출금',
 '413000', '2', '출금(이체)', '1037'),
-- Record 38
(NOW(), NOW(), NULL, 470000, 460000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240420', 'Coffee purchase', 'COFFEE출금',
 '423000', '2', '출금(이체)', '1038'),
-- Record 39
(NOW(), NOW(), NULL, 480000, 470000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240525', 'Drink purchase', 'DRINK출금',
 '433000', '2', '출금(이체)', '1039'),
-- Record 40
(NOW(), NOW(), NULL, 490000, 480000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240610', 'Delivery service', 'DELIVERY출금',
 '443000', '2', '출금(이체)', '1040'),
-- Record 41
(NOW(), NOW(), NULL, 500000, 490000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240705', 'Coffee purchase', 'COFFEE출금',
 '453000', '2', '출금(이체)', '1041'),
-- Record 42
(NOW(), NOW(), NULL, 510000, 500000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240801', 'Drink purchase', 'DRINK출금',
 '463000', '2', '출금(이체)', '1042'),
-- Record 43
(NOW(), NOW(), NULL, 520000, 510000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240315', 'Delivery service', 'DELIVERY출금',
 '473000', '2', '출금(이체)', '1043'),
-- Record 44
(NOW(), NOW(), NULL, 530000, 520000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240410', 'Coffee purchase', 'COFFEE출금',
 '483000', '2', '출금(이체)', '1044'),
-- Record 45
(NOW(), NOW(), NULL, 540000, 530000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240505', 'Drink purchase', 'DRINK출금',
 '493000', '2', '출금(이체)', '1045'),
-- Record 46
(NOW(), NOW(), NULL, 550000, 540000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240601', 'Delivery service', 'DELIVERY출금',
 '503000', '2', '출금(이체)', '1046'),
-- Record 47
(NOW(), NOW(), NULL, 560000, 550000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240710', 'Coffee purchase', 'COFFEE출금',
 '513000', '2', '출금(이체)', '1047'),
-- Record 48
(NOW(), NOW(), NULL, 570000, 560000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240801', 'Drink purchase', 'DRINK출금',
 '523000', '2', '출금(이체)', '1048'),
-- Record 49
(NOW(), NOW(), NULL, 580000, 570000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240320', 'Delivery service', 'DELIVERY출금',
 '533000', '2', '출금(이체)', '1049'),
-- Record 50
(NOW(), NOW(), NULL, 590000, 580000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240415', 'Coffee purchase', 'COFFEE출금',
 '543000', '2', '출금(이체)', '1050');

-- SQL Insert statements for the consumption table with "COFFEE출금" transactions from March to August

INSERT INTO DONG.consumption (
    created_at, updated_at, deleted_at,
    transaction_after_balance, transaction_balance,
    id, member_id, transaction_account_no,
    transaction_date, transaction_memo, transaction_summary,
    transaction_time, transaction_type, transaction_type_name,
    transaction_unique_no
) VALUES
-- Record 1
(NOW(), NOW(), NULL, 100000, 95400,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240301', 'Coffee purchase', 'COFFEE출금',
 '103220', '2', '출금(이체)', '1001'),
-- Record 2
(NOW(), NOW(), NULL, 105000, 100500,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240315', 'Coffee purchase', 'COFFEE출금',
 '112030', '2', '출금(이체)', '1002'),
-- Record 3
(NOW(), NOW(), NULL, 110000, 105100,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240330', 'Coffee purchase', 'COFFEE출금',
 '130045', '2', '출금(이체)', '1003'),
-- Record 4
(NOW(), NOW(), NULL, 115000, 112000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240410', 'Coffee purchase', 'COFFEE출금',
 '141500', '2', '출금(이체)', '1004'),
-- Record 5
(NOW(), NOW(), NULL, 120000, 115100,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240420', 'Coffee purchase', 'COFFEE출금',
 '153000', '2', '출금(이체)', '1005'),
-- Record 6
(NOW(), NOW(), NULL, 125000, 120300,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240501', 'Coffee purchase', 'COFFEE출금',
 '110000', '2', '출금(이체)', '1006'),
-- Record 7
(NOW(), NOW(), NULL, 130000, 125500,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240510', 'Coffee purchase', 'COFFEE출금',
 '123000', '2', '출금(이체)', '1007'),
-- Record 8
(NOW(), NOW(), NULL, 135000, 131000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240520', 'Coffee purchase', 'COFFEE출금',
 '140000', '2', '출금(이체)', '1008'),
-- Record 9
(NOW(), NOW(), NULL, 140000, 135100,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240601', 'Coffee purchase', 'COFFEE출금',
 '153000', '2', '출금(이체)', '1009'),
-- Record 10
(NOW(), NOW(), NULL, 145000, 140400,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240610', 'Coffee purchase', 'COFFEE출금',
 '160000', '2', '출금(이체)', '1010'),
-- Record 11
(NOW(), NOW(), NULL, 150000, 145200,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240620', 'Coffee purchase', 'COFFEE출금',
 '113000', '2', '출금(이체)', '1011'),
-- Record 12
(NOW(), NOW(), NULL, 155000, 150500,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240701', 'Coffee purchase', 'COFFEE출금',
 '124500', '2', '출금(이체)', '1012'),
-- Record 13
(NOW(), NOW(), NULL, 160000, 155200,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240710', 'Coffee purchase', 'COFFEE출금',
 '140000', '2', '출금(이체)', '1013'),
-- Record 14
(NOW(), NOW(), NULL, 165000, 160700,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240720', 'Coffee purchase', 'COFFEE출금',
 '151500', '2', '출금(이체)', '1014'),
-- Record 15
(NOW(), NOW(), NULL, 170000, 165400,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240801', 'Coffee purchase', 'COFFEE출금',
 '110000', '2', '출금(이체)', '1015'),
-- Record 16
(NOW(), NOW(), NULL, 175000, 170200,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240805', 'Coffee purchase', 'COFFEE출금',
 '121500', '2', '출금(이체)', '1016'),
-- Record 17
(NOW(), NOW(), NULL, 180000, 175800,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240810', 'Coffee purchase', 'COFFEE출금',
 '134500', '2', '출금(이체)', '1017'),
-- Record 18
(NOW(), NOW(), NULL, 185000, 180800,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240303', 'Coffee purchase', 'COFFEE출금',
 '150000', '2', '출금(이체)', '1018'),
-- Record 19
(NOW(), NOW(), NULL, 190000, 185600,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240318', 'Coffee purchase', 'COFFEE출금',
 '163000', '2', '출금(이체)', '1019'),
-- Record 20
(NOW(), NOW(), NULL, 195000, 190500,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240401', 'Coffee purchase', 'COFFEE출금',
 '120000', '2', '출금(이체)', '1020'),
-- Record 21
(NOW(), NOW(), NULL, 200000, 195400,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240412', 'Coffee purchase', 'COFFEE출금',
 '134500', '2', '출금(이체)', '1021'),
-- Record 22
(NOW(), NOW(), NULL, 205000, 202000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240425', 'Coffee purchase', 'COFFEE출금',
 '150000', '2', '출금(이체)', '1022'),
-- Record 23
(NOW(), NOW(), NULL, 210000, 205600,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240505', 'Coffee purchase', 'COFFEE출금',
 '161500', '2', '출금(이체)', '1023'),
-- Record 24
(NOW(), NOW(), NULL, 215000, 210600,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240515', 'Coffee purchase', 'COFFEE출금',
 '175000', '2', '출금(이체)', '1024'),
-- Record 25
(NOW(), NOW(), NULL, 220000, 215600,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240525', 'Coffee purchase', 'COFFEE출금',
 '183000', '2', '출금(이체)', '1025'),
-- Record 26
(NOW(), NOW(), NULL, 225000, 220400,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240602', 'Coffee purchase', 'COFFEE출금',
 '190000', '2', '출금(이체)', '1026'),
-- Record 27
(NOW(), NOW(), NULL, 230000, 225200,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240612', 'Coffee purchase', 'COFFEE출금',
 '203000', '2', '출금(이체)', '1027'),
-- Record 28
(NOW(), NOW(), NULL, 235000, 230800,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240622', 'Coffee purchase', 'COFFEE출금',
 '215000', '2', '출금(이체)', '1028'),
-- Record 29
(NOW(), NOW(), NULL, 240000, 235800,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240701', 'Coffee purchase', 'COFFEE출금',
 '225000', '2', '출금(이체)', '1029'),
-- Record 30
(NOW(), NOW(), NULL, 245000, 240700,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240710', 'Coffee purchase', 'COFFEE출금',
 '235000', '2', '출금(이체)', '1030'),
-- Record 31
(NOW(), NOW(), NULL, 250000, 245400,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240720', 'Coffee purchase', 'COFFEE출금',
 '250000', '2', '출금(이체)', '1031'),
-- Record 32
(NOW(), NOW(), NULL, 255000, 250400,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240730', 'Coffee purchase', 'COFFEE출금',
 '260000', '2', '출금(이체)', '1032'),
-- Record 33
(NOW(), NOW(), NULL, 260000, 255700,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240805', 'Coffee purchase', 'COFFEE출금',
 '270000', '2', '출금(이체)', '1033'),
-- Record 34
(NOW(), NOW(), NULL, 265000, 260300,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240810', 'Coffee purchase', 'COFFEE출금',
 '280000', '2', '출금(이체)', '1034'),
-- Record 35
(NOW(), NOW(), NULL, 270000, 265500,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240815', 'Coffee purchase', 'COFFEE출금',
 '290000', '2', '출금(이체)', '1035'),
-- Record 36
(NOW(), NOW(), NULL, 275000, 271000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240305', 'Coffee purchase', 'COFFEE출금',
 '300000', '2', '출금(이체)', '1036'),
-- Record 37
(NOW(), NOW(), NULL, 280000, 275100,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240315', 'Coffee purchase', 'COFFEE출금',
 '310000', '2', '출금(이체)', '1037'),
-- Record 38
(NOW(), NOW(), NULL, 285000, 280300,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240325', 'Coffee purchase', 'COFFEE출금',
 '320000', '2', '출금(이체)', '1038'),
-- Record 39
(NOW(), NOW(), NULL, 290000, 285800,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240401', 'Coffee purchase', 'COFFEE출금',
 '330000', '2', '출금(이체)', '1039'),
-- Record 40
(NOW(), NOW(), NULL, 295000, 290600,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240410', 'Coffee purchase', 'COFFEE출금',
 '340000', '2', '출금(이체)', '1040'),
-- Record 41
(NOW(), NOW(), NULL, 300000, 295400,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240415', 'Coffee purchase', 'COFFEE출금',
 '350000', '2', '출금(이체)', '1041'),
-- Record 42
(NOW(), NOW(), NULL, 305000, 300300,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240501', 'Coffee purchase', 'COFFEE출금',
 '360000', '2', '출금(이체)', '1042'),
-- Record 43
(NOW(), NOW(), NULL, 310000, 305200,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240510', 'Coffee purchase', 'COFFEE출금',
 '370000', '2', '출금(이체)', '1043'),
-- Record 44
(NOW(), NOW(), NULL, 315000, 312000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240515', 'Coffee purchase', 'COFFEE출금',
 '380000', '2', '출금(이체)', '1044'),
-- Record 45
(NOW(), NOW(), NULL, 320000, 315300,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240520', 'Coffee purchase', 'COFFEE출금',
 '390000', '2', '출금(이체)', '1045'),
-- Record 46
(NOW(), NOW(), NULL, 325000, 320200,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240601', 'Coffee purchase', 'COFFEE출금',
 '400000', '2', '출금(이체)', '1046'),
-- Record 47
(NOW(), NOW(), NULL, 330000, 325500,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240610', 'Coffee purchase', 'COFFEE출금',
 '410000', '2', '출금(이체)', '1047'),
-- Record 48
(NOW(), NOW(), NULL, 335000, 330800,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240620', 'Coffee purchase', 'COFFEE출금',
 '420000', '2', '출금(이체)', '1048'),
-- Record 49
(NOW(), NOW(), NULL, 340000, 335800,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240701', 'Coffee purchase', 'COFFEE출금',
 '430000', '2', '출금(이체)', '1049'),
-- Record 50
(NOW(), NOW(), NULL, 345000, 342000,
 UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160',
 '20240710', 'Coffee purchase', 'COFFEE출금',
 '440000', '2', '출금(이체)', '1050');

-- Insert 50 records with varying amounts for DELIVERY 출금
INSERT INTO DONG.consumption
(created_at, updated_at, deleted_at, transaction_balance, transaction_after_balance, id, member_id, transaction_account_no, transaction_date, transaction_memo, transaction_summary, transaction_time, transaction_type, transaction_type_name, transaction_unique_no)
VALUES
-- Record 1
(NOW(), NOW(), NULL, 1000000, 983000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240301', 'Delivery payment', 'DELIVERY출금', '093000', '2', '출금(이체)', '2001'),
-- Record 2
(NOW(), NOW(), NULL, 983000, 967000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240305', 'Delivery payment', 'DELIVERY출금', '103000', '2', '출금(이체)', '2002'),
-- Record 3
(NOW(), NOW(), NULL, 967000, 950500, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240310', 'Delivery payment', 'DELIVERY출금', '113500', '2', '출금(이체)', '2003'),
-- Record 4
(NOW(), NOW(), NULL, 950500, 934500, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240315', 'Delivery payment', 'DELIVERY출금', '123000', '2', '출금(이체)', '2004'),
-- Record 5
(NOW(), NOW(), NULL, 934500, 918000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240320', 'Delivery payment', 'DELIVERY출금', '133500', '2', '출금(이체)', '2005'),
-- Record 6
(NOW(), NOW(), NULL, 918000, 902000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240325', 'Delivery payment', 'DELIVERY출금', '143000', '2', '출금(이체)', '2006'),
-- Record 7
(NOW(), NOW(), NULL, 902000, 887000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240330', 'Delivery payment', 'DELIVERY출금', '153500', '2', '출금(이체)', '2007'),
-- Record 8
(NOW(), NOW(), NULL, 887000, 872000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240402', 'Delivery payment', 'DELIVERY출금', '163500', '2', '출금(이체)', '2008'),
-- Record 9
(NOW(), NOW(), NULL, 872000, 857000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240405', 'Delivery payment', 'DELIVERY출금', '173000', '2', '출금(이체)', '2009'),
-- Record 10
(NOW(), NOW(), NULL, 857000, 842000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240410', 'Delivery payment', 'DELIVERY출금', '183500', '2', '출금(이체)', '2010'),
-- Record 11
(NOW(), NOW(), NULL, 842000, 827000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240415', 'Delivery payment', 'DELIVERY출금', '193000', '2', '출금(이체)', '2011'),
-- Record 12
(NOW(), NOW(), NULL, 827000, 812000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240420', 'Delivery payment', 'DELIVERY출금', '203500', '2', '출금(이체)', '2012'),
-- Record 13
(NOW(), NOW(), NULL, 812000, 797000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240425', 'Delivery payment', 'DELIVERY출금', '213500', '2', '출금(이체)', '2013'),
-- Record 14
(NOW(), NOW(), NULL, 797000, 782000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240430', 'Delivery payment', 'DELIVERY출금', '223500', '2', '출금(이체)', '2014'),
-- Record 15
(NOW(), NOW(), NULL, 782000, 767000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240501', 'Delivery payment', 'DELIVERY출금', '233500', '2', '출금(이체)', '2015'),
-- Record 16
(NOW(), NOW(), NULL, 767000, 752000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240505', 'Delivery payment', 'DELIVERY출금', '243000', '2', '출금(이체)', '2016'),
-- Record 17
(NOW(), NOW(), NULL, 752000, 737000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240510', 'Delivery payment', 'DELIVERY출금', '253500', '2', '출금(이체)', '2017'),
-- Record 18
(NOW(), NOW(), NULL, 737000, 722000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240515', 'Delivery payment', 'DELIVERY출금', '263500', '2', '출금(이체)', '2018'),
-- Record 19
(NOW(), NOW(), NULL, 722000, 707000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240520', 'Delivery payment', 'DELIVERY출금', '273500', '2', '출금(이체)', '2019'),
-- Record 20
(NOW(), NOW(), NULL, 707000, 692000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240525', 'Delivery payment', 'DELIVERY출금', '283500', '2', '출금(이체)', '2020'),
-- Record 21
(NOW(), NOW(), NULL, 692000, 677000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240601', 'Delivery payment', 'DELIVERY출금', '293000', '2', '출금(이체)', '2021'),
-- Record 22
(NOW(), NOW(), NULL, 677000, 662000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240605', 'Delivery payment', 'DELIVERY출금', '303000', '2', '출금(이체)', '2022'),
-- Record 23
(NOW(), NOW(), NULL, 662000, 647000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240610', 'Delivery payment', 'DELIVERY출금', '313500', '2', '출금(이체)', '2023'),
-- Record 24
(NOW(), NOW(), NULL, 647000, 632000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240615', 'Delivery payment', 'DELIVERY출금', '323500', '2', '출금(이체)', '2024'),
-- Record 25
(NOW(), NOW(), NULL, 632000, 617000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240620', 'Delivery payment', 'DELIVERY출금', '333500', '2', '출금(이체)', '2025'),
-- Record 26
(NOW(), NOW(), NULL, 617000, 602000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240625', 'Delivery payment', 'DELIVERY출금', '343500', '2', '출금(이체)', '2026'),
-- Record 27
(NOW(), NOW(), NULL, 602000, 587000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240701', 'Delivery payment', 'DELIVERY출금', '353500', '2', '출금(이체)', '2027'),
-- Record 28
(NOW(), NOW(), NULL, 587000, 572000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240705', 'Delivery payment', 'DELIVERY출금', '363500', '2', '출금(이체)', '2028'),
-- Record 29
(NOW(), NOW(), NULL, 572000, 557000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240710', 'Delivery payment', 'DELIVERY출금', '373500', '2', '출금(이체)', '2029'),
-- Record 30
(NOW(), NOW(), NULL, 557000, 542000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240715', 'Delivery payment', 'DELIVERY출금', '383500', '2', '출금(이체)', '2030'),
-- Record 31
(NOW(), NOW(), NULL, 542000, 527000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240720', 'Delivery payment', 'DELIVERY출금', '393500', '2', '출금(이체)', '2031'),
-- Record 32
(NOW(), NOW(), NULL, 527000, 512000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240725', 'Delivery payment', 'DELIVERY출금', '403500', '2', '출금(이체)', '2032'),
-- Record 33
(NOW(), NOW(), NULL, 512000, 497000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240801', 'Delivery payment', 'DELIVERY출금', '413500', '2', '출금(이체)', '2033'),
-- Record 34
(NOW(), NOW(), NULL, 497000, 482000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240805', 'Delivery payment', 'DELIVERY출금', '423500', '2', '출금(이체)', '2034'),
-- Record 35
(NOW(), NOW(), NULL, 482000, 467000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240810', 'Delivery payment', 'DELIVERY출금', '433500', '2', '출금(이체)', '2035'),
-- Record 36
(NOW(), NOW(), NULL, 467000, 452000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240815', 'Delivery payment', 'DELIVERY출금', '443500', '2', '출금(이체)', '2036'),
-- Record 37
(NOW(), NOW(), NULL, 452000, 437000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240820', 'Delivery payment', 'DELIVERY출금', '453500', '2', '출금(이체)', '2037'),
-- Record 38
(NOW(), NOW(), NULL, 437000, 422000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240825', 'Delivery payment', 'DELIVERY출금', '463500', '2', '출금(이체)', '2038'),
-- Record 39
(NOW(), NOW(), NULL, 422000, 407000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240830', 'Delivery payment', 'DELIVERY출금', '473500', '2', '출금(이체)', '2039'),
-- Record 40
(NOW(), NOW(), NULL, 407000, 392000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240901', 'Delivery payment', 'DELIVERY출금', '483500', '2', '출금(이체)', '2040'),
-- Record 41
(NOW(), NOW(), NULL, 392000, 377000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240905', 'Delivery payment', 'DELIVERY출금', '493500', '2', '출금(이체)', '2041'),
-- Record 42
(NOW(), NOW(), NULL, 377000, 362000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240910', 'Delivery payment', 'DELIVERY출금', '503500', '2', '출금(이체)', '2042'),
-- Record 43
(NOW(), NOW(), NULL, 362000, 347000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240915', 'Delivery payment', 'DELIVERY출금', '513500', '2', '출금(이체)', '2043');

INSERT INTO DONG.consumption (
    created_at, updated_at, deleted_at, transaction_balance,
    transaction_after_balance, id, member_id,
    transaction_account_no, transaction_date, transaction_memo,
    transaction_summary, transaction_unique_no, transaction_type,
    transaction_type_name
) VALUES
-- Record 1
(NOW(), NOW(), NULL, 1000000, 986000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240301', 'Drink payment', 'DRINK출금', '2051', '2', '출금(이체)'),
-- Record 2
(NOW(), NOW(), NULL, 986000, 974500, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240305', 'Drink payment', 'DRINK출금', '2052', '2', '출금(이체)'),
-- Record 3
(NOW(), NOW(), NULL, 974500, 960500, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240310', 'Drink payment', 'DRINK출금', '2053', '2', '출금(이체)'),
-- Record 4
(NOW(), NOW(), NULL, 960500, 945000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240315', 'Drink payment', 'DRINK출금', '2054', '2', '출금(이체)'),
-- Record 5
(NOW(), NOW(), NULL, 945000, 932000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240320', 'Drink payment', 'DRINK출금', '2055', '2', '출금(이체)'),
-- Record 6
(NOW(), NOW(), NULL, 932000, 918500, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240325', 'Drink payment', 'DRINK출금', '2056', '2', '출금(이체)'),
-- Record 7
(NOW(), NOW(), NULL, 918500, 905000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240330', 'Drink payment', 'DRINK출금', '2057', '2', '출금(이체)'),
-- Record 8
(NOW(), NOW(), NULL, 905000, 890000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240405', 'Drink payment', 'DRINK출금', '2058', '2', '출금(이체)'),
-- Record 9
(NOW(), NOW(), NULL, 890000, 878000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240410', 'Drink payment', 'DRINK출금', '2059', '2', '출금(이체)'),
-- Record 10
(NOW(), NOW(), NULL, 878000, 864000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240415', 'Drink payment', 'DRINK출금', '2060', '2', '출금(이체)'),
-- Record 11
(NOW(), NOW(), NULL, 864000, 850000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240420', 'Drink payment', 'DRINK출금', '2061', '2', '출금(이체)'),
-- Record 12
(NOW(), NOW(), NULL, 850000, 836000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240425', 'Drink payment', 'DRINK출금', '2062', '2', '출금(이체)'),
-- Record 13
(NOW(), NOW(), NULL, 836000, 822000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240501', 'Drink payment', 'DRINK출금', '2063', '2', '출금(이체)'),
-- Record 14
(NOW(), NOW(), NULL, 822000, 808000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240505', 'Drink payment', 'DRINK출금', '2064', '2', '출금(이체)'),
-- Record 15
(NOW(), NOW(), NULL, 808000, 794000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240510', 'Drink payment', 'DRINK출금', '2065', '2', '출금(이체)'),
-- Record 16
(NOW(), NOW(), NULL, 794000, 780000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240515', 'Drink payment', 'DRINK출금', '2066', '2', '출금(이체)'),
-- Record 17
(NOW(), NOW(), NULL, 780000, 766000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240520', 'Drink payment', 'DRINK출금', '2067', '2', '출금(이체)'),
-- Record 18
(NOW(), NOW(), NULL, 766000, 752000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240525', 'Drink payment', 'DRINK출금', '2068', '2', '출금(이체)'),
-- Record 19
(NOW(), NOW(), NULL, 752000, 738000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240530', 'Drink payment', 'DRINK출금', '2069', '2', '출금(이체)'),
-- Record 20
(NOW(), NOW(), NULL, 738000, 724000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240605', 'Drink payment', 'DRINK출금', '2070', '2', '출금(이체)'),
-- Record 21
(NOW(), NOW(), NULL, 724000, 710000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240610', 'Drink payment', 'DRINK출금', '2071', '2', '출금(이체)'),
-- Record 22
(NOW(), NOW(), NULL, 710000, 696000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240615', 'Drink payment', 'DRINK출금', '2072', '2', '출금(이체)'),
-- Record 23
(NOW(), NOW(), NULL, 696000, 682000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240620', 'Drink payment', 'DRINK출금', '2073', '2', '출금(이체)'),
-- Record 24
(NOW(), NOW(), NULL, 682000, 668000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240625', 'Drink payment', 'DRINK출금', '2074', '2', '출금(이체)'),
-- Record 25
(NOW(), NOW(), NULL, 668000, 654000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240630', 'Drink payment', 'DRINK출금', '2075', '2', '출금(이체)'),
-- Record 26
(NOW(), NOW(), NULL, 654000, 640000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240705', 'Drink payment', 'DRINK출금', '2076', '2', '출금(이체)'),
-- Record 27
(NOW(), NOW(), NULL, 640000, 626000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240710', 'Drink payment', 'DRINK출금', '2077', '2', '출금(이체)'),
-- Record 28
(NOW(), NOW(), NULL, 626000, 612000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240715', 'Drink payment', 'DRINK출금', '2078', '2', '출금(이체)'),
-- Record 29
(NOW(), NOW(), NULL, 612000, 598000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240720', 'Drink payment', 'DRINK출금', '2079', '2', '출금(이체)'),
-- Record 30
(NOW(), NOW(), NULL, 598000, 584000, UNHEX(REPLACE(UUID(), '-', '')), 0x01919C5FBAC69C61BAD76CE5F992440E, '0881367640491160', '20240725', 'Drink payment', 'DRINK출금', '2080', '2', '출금(이체)');
