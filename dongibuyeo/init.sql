DROP
    DATABASE IF EXISTS `DONG`;

CREATE
    DATABASE `DONG` DEFAULT CHARACTER
    SET utf8 COLLATE utf8_unicode_ci;

USE `DONG`;

CREATE TABLE DONG.`member`
(
    `created_at`    datetime(6)  DEFAULT NULL,
    `deleted_at`    datetime(6)  DEFAULT NULL,
    `name`          varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci  NOT NULL,
    `updated_at`    datetime(6)  DEFAULT NULL,
    `nickname`      varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci  NOT NULL,
    `id`            binary(16)                                              NOT NULL,
    `device_token`  varchar(512) DEFAULT NULL,
    `email`         varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
    `profile_image` varchar(255) DEFAULT NULL,
    `user_key`      varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


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
    KEY `FKr5j0huynd7nsv1s7e9vb8qvwo` (`member_id`),
    CONSTRAINT `FKr5j0huynd7nsv1s7e9vb8qvwo` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

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
    `description`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci                                          DEFAULT NULL,
    `image`         varchar(255)                                                                                           DEFAULT NULL,
    `title`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci                                          DEFAULT NULL,
    `type`          enum ('CONSUMPTION_COFFEE','CONSUMPTION_DELIVERY','CONSUMPTION_DRINK','QUIZ_SOLBEING','SAVINGS_SEVEN') DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK4saeeac119vkfictd49yb5g67` (`account_id`),
    CONSTRAINT `FKwbq44q4pl5dp1j6q0y5sjg7y` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
    CONSTRAINT `challenge_chk_1` CHECK ((`status` between 0 and 2))
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

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
    KEY `FKh9n4f0bidmjun3fvk2jp5netm` (`challenge_id`),
    KEY `FK9x810nqdrhsdpp78017y3kqhe` (`member_id`),
    CONSTRAINT `FK9x810nqdrhsdpp78017y3kqhe` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`),
    CONSTRAINT `FKh9n4f0bidmjun3fvk2jp5netm` FOREIGN KEY (`challenge_id`) REFERENCES `challenge` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

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
    KEY `FKra4hpn2ur20o0p56dsvdodj94` (`member_id`),
    CONSTRAINT `FKra4hpn2ur20o0p56dsvdodj94` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE DONG.`daily_score`
(
    `date`                date       DEFAULT NULL,
    `total_score`         int        NOT NULL,
    `id`                  binary(16) NOT NULL,
    `member_challenge_id` binary(16) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK36en58l32c8r7ei87cl4gsii3` (`member_challenge_id`),
    CONSTRAINT `FK36en58l32c8r7ei87cl4gsii3` FOREIGN KEY (`member_challenge_id`) REFERENCES `member_challenge` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE DONG.`room`
(
    `created_at` datetime(6)  DEFAULT NULL,
    `deleted_at` datetime(6)  DEFAULT NULL,
    `updated_at` datetime(6)  DEFAULT NULL,
    `id`         binary(16) NOT NULL,
    `name`       varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE DONG.`message`
(
    `created_at` datetime(6)  DEFAULT NULL,
    `deleted_at` datetime(6)  DEFAULT NULL,
    `updated_at` datetime(6)  DEFAULT NULL,
    `id`         binary(16) NOT NULL,
    `member_id`  binary(16)   DEFAULT NULL,
    `room_id`    binary(16)   DEFAULT NULL,
    `message`    varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK4msccpwmxulmw875edu7p352d` (`member_id`),
    KEY `FKl1kg5a2471cv6pkew0gdgjrmo` (`room_id`),
    CONSTRAINT `FK4msccpwmxulmw875edu7p352d` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`),
    CONSTRAINT `FKl1kg5a2471cv6pkew0gdgjrmo` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

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
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE DONG.`quiz`
(
    `answer`     bit(1)       DEFAULT NULL,
    `created_at` datetime(6)  DEFAULT NULL,
    `deleted_at` datetime(6)  DEFAULT NULL,
    `updated_at` datetime(6)  DEFAULT NULL,
    `id`         binary(16) NOT NULL,
    `question`   varchar(512) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

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
    KEY `FK12peccx2dtulmtm31oofjbw61` (`member_id`),
    KEY `FKe5ckx4p03i3utc2a413uaxahr` (`quiz_id`),
    CONSTRAINT `FK12peccx2dtulmtm31oofjbw61` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`),
    CONSTRAINT `FKe5ckx4p03i3utc2a413uaxahr` FOREIGN KEY (`quiz_id`) REFERENCES `quiz` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


CREATE TABLE DONG.`score_entries`
(
    `current_total_score` int          DEFAULT NULL,
    `score`               int          DEFAULT NULL,
    `daily_score_id`      binary(16) NOT NULL,
    `description`         varchar(255) DEFAULT NULL,
    KEY `FKe23oh1ywc5mgk76qost22vc6q` (`daily_score_id`),
    CONSTRAINT `FKe23oh1ywc5mgk76qost22vc6q` FOREIGN KEY (`daily_score_id`) REFERENCES `daily_score` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

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
        'admin@dongibuyeo-test.com', NULL, '113c59ec-b974-4f04-984f-6642566d4856'),
       ('2024-08-29 12:54:45', NULL, '박수진', '2024-08-29 12:54:45', '박박박', 0x01919C457DCC847749819F669AB5DD46, '',
        'sujin1@naver.com', NULL, '7e72acce-c2a3-4a67-be11-aa9702156257'),
       ('2024-08-29 13:06:37', NULL, '신한', '2024-08-29 14:27:48', '해커톤시렁', 0x01919C505AF68AE77DFFEA6E329C5A6D, '',
        'asd@asd.com', 'Lay', '534effa4-1573-4fb0-a857-aea69ca6568d'),
       ('2024-08-29 13:23:25', NULL, '김신한', '2024-08-29 13:23:25', 'shinhanKim', 0x01919C5FBAC69C61BAD76CE5F992440E, '',
        'shinhanKim@dongibuyeo-test.com', '', '5971b18f-bfd6-49a4-b1c8-883d70ebb803'),
       ('2024-08-29 14:53:56', NULL, '곽곽', '2024-08-29 14:53:56', '곽곽이', 0x01919CB29B1033C9DCF879E53C3B31F2, '',
        'test123@test.co', NULL, 'cbe35d3c-caed-42ca-8caf-eaa25081ee94');

# INSERT INTO DONG.member (id, email, name, nickname, profile_image, user_key, device_token, created_at, updated_at)
# SELECT UNHEX(REPLACE(UUID(), '-', '')),
#        CONCAT('user', n, '@dongibuyeo.com'),
#        CONCAT('User', n),
#        CONCAT('Nickname', n),
#        NULL,
#        'cbe35d3c-caed-42ca-8caf-eaa25081ee94',
#        CONCAT('device_token_', n),
#        DATE_ADD('2024-01-01', INTERVAL n DAY),
#        DATE_ADD('2024-01-01', INTERVAL n DAY)
# FROM (SELECT @row := @row + 1 AS n
#       FROM (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL
#             SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) t1,
#            (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL
#             SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) t2,
#            (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL
#             SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) t3,
#            (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2) t4,
#            (SELECT @row := 0) r) numbers
# WHERE n <= 3000;

INSERT INTO DONG.account (created_at, deleted_at, updated_at, id, member_id, account_no, account_type)
VALUES ('2024-08-29 13:24:02', NULL, '2024-08-29 13:24:02.964314', 0x01919C604E86334E71442B0E6AA8B8AE,
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
       (0x00, 0, 0, 0, '2024-08-29 23:50:00', NULL, 70000, '2024-08-30 00:05:00',
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

-- 모든 챌린지에 대해 멤버 챌린지 생성 (2000-3000명)
INSERT INTO DONG.member_challenge (is_success, total_score, additional_reward, base_reward, created_at, deleted_at,
                                   deposit, updated_at, challenge_id, id, member_id, status)
WITH RECURSIVE challenge_participants AS (
    SELECT
        c.id AS challenge_id,
        2000 + FLOOR(RAND() * 1001) AS target_participants
    FROM DONG.challenge c
),
               member_pool AS (
                   SELECT id FROM DONG.member WHERE email LIKE 'user%@dongibuyeo.com'
               ),
               challenge_members AS (
                   SELECT
                       cp.challenge_id,
                       m.id AS member_id,
                       cp.target_participants,
                       ROW_NUMBER() OVER (PARTITION BY cp.challenge_id ORDER BY RAND()) AS row_num
                   FROM
                       challenge_participants cp
                           CROSS JOIN member_pool m
               )
SELECT
    CASE
        WHEN c.status = 2 THEN CAST(RAND() > 0.5 AS SIGNED)
        ELSE 0
        END,
    CASE
        WHEN c.status = 2 THEN FLOOR(RAND() * 1000)
        ELSE FLOOR(RAND() * 100)
        END,
    CASE
        WHEN c.status = 2 THEN FLOOR(RAND() * 5000)
        ELSE 0
        END,
    CASE
        WHEN c.status = 2 THEN 10000
        ELSE 0
        END,
    c.start_date,
    NULL,
    CASE
        WHEN c.type = 'SAVINGS_SEVEN' THEN 70000
        ELSE FLOOR(RAND() * 30) * 10000 + 10000 -- 10,000원부터 300,000원까지 10,000원 단위
        END,
    CASE
        WHEN c.status = 2 THEN c.end_date
        ELSE c.start_date
        END,
    c.id,
    UNHEX(REPLACE(UUID(), '-', '')),
    cm.member_id,
    CASE
        WHEN c.status = 2 THEN 'CALCULATED'
        WHEN c.status = 1 THEN 'BEFORE_CALCULATION'
        ELSE 'BEFORE_CALCULATION'
        END
FROM
    DONG.challenge c
        JOIN challenge_members cm ON c.id = cm.challenge_id
WHERE
    cm.row_num <= cm.target_participants;

-- DailyScore 데이터 생성 (IN_PROGRESS 및 COMPLETED 챌린지만)
INSERT INTO DONG.daily_score (id, member_challenge_id, date, total_score)
SELECT UNHEX(MD5(CONCAT(mc.id, DATE_ADD(c.start_date, INTERVAL seq.seq DAY)))),
       mc.id,
       DATE_ADD(c.start_date, INTERVAL seq.seq DAY),
       0 -- 초기 총점은 0으로 설정
FROM DONG.member_challenge mc
         JOIN DONG.challenge c ON mc.challenge_id = c.id
         JOIN (SELECT a.N + b.N * 10 + c.N * 100 AS seq
               FROM (SELECT 0 AS N
                     UNION
                     SELECT 1
                     UNION
                     SELECT 2
                     UNION
                     SELECT 3
                     UNION
                     SELECT 4
                     UNION
                     SELECT 5
                     UNION
                     SELECT 6
                     UNION
                     SELECT 7
                     UNION
                     SELECT 8
                     UNION
                     SELECT 9) a
                        CROSS JOIN (SELECT 0 AS N
                                    UNION
                                    SELECT 1
                                    UNION
                                    SELECT 2
                                    UNION
                                    SELECT 3
                                    UNION
                                    SELECT 4
                                    UNION
                                    SELECT 5
                                    UNION
                                    SELECT 6
                                    UNION
                                    SELECT 7
                                    UNION
                                    SELECT 8
                                    UNION
                                    SELECT 9) b
                        CROSS JOIN (SELECT 0 AS N
                                    UNION
                                    SELECT 1
                                    UNION
                                    SELECT 2
                                    UNION
                                    SELECT 3
                                    UNION
                                    SELECT 4
                                    UNION
                                    SELECT 5
                                    UNION
                                    SELECT 6
                                    UNION
                                    SELECT 7
                                    UNION
                                    SELECT 8
                                    UNION
                                    SELECT 9) c) seq
WHERE DATE_ADD(c.start_date, INTERVAL seq.seq DAY) <= LEAST(c.end_date, CURDATE());

-- DAILY_SCORE 생성
INSERT INTO DONG.score_entries (daily_score_id, description, score, current_total_score)
SELECT ds.id,
       'DAILY_SCORE',
       10,
       0
FROM DONG.daily_score ds;

-- 챌린지 타입별 추가 점수 생성
INSERT INTO DONG.score_entries (daily_score_id, description, score, current_total_score)
SELECT ds.id,
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
       0       AS current_total_score
FROM DONG.daily_score ds
         JOIN DONG.member_challenge mc ON ds.member_challenge_id = mc.id
         JOIN DONG.challenge c ON mc.challenge_id = c.id
WHERE CASE
          WHEN c.type IN ('CONSUMPTION_COFFEE', 'CONSUMPTION_DELIVERY', 'CONSUMPTION_DRINK') THEN RAND() < 0.7
          WHEN c.type = 'QUIZ_SOLBEING' THEN RAND() < 0.8
          WHEN c.type = 'SAVINGS_SEVEN' THEN RAND() < 0.95
          ELSE FALSE
          END;

-- current_total_score 업데이트 (챌린지 시작부터 현재 항목까지의 누적 점수)
UPDATE DONG.score_entries se
    JOIN (SELECT se2.daily_score_id,
                 se2.description,
                 @running_total := @running_total + se2.score AS current_total_score
          FROM (SELECT *
                FROM DONG.score_entries
                         JOIN DONG.daily_score ON DONG.score_entries.daily_score_id = DONG.daily_score.id
                ORDER BY DONG.daily_score.member_challenge_id, DONG.daily_score.date,
                         DONG.score_entries.description) se2,
               (SELECT @running_total := 0) vars) AS calc
SET se.current_total_score = calc.current_total_score
WHERE se.daily_score_id = calc.daily_score_id
  AND se.description = calc.description;

-- DailyScore의 total_score 업데이트
UPDATE DONG.daily_score ds
    JOIN (SELECT daily_score_id, MAX(current_total_score) AS max_score
          FROM DONG.score_entries
          GROUP BY daily_score_id) se ON ds.id = se.daily_score_id
SET ds.total_score = se.max_score
WHERE ds.total_score != se.max_score
   OR ds.total_score IS NULL;

-- MemberChallenge의 total_score 업데이트
UPDATE DONG.member_challenge mc
    JOIN (SELECT member_challenge_id, MAX(total_score) AS max_total_score
          FROM DONG.daily_score
          GROUP BY member_challenge_id) ds ON mc.id = ds.member_challenge_id
SET mc.total_score = ds.max_total_score
WHERE mc.total_score != ds.max_total_score
   OR mc.total_score IS NULL;

-- Challenge의 participants와 total_deposit 업데이트
UPDATE DONG.challenge c
    JOIN (SELECT challenge_id,
                 COUNT(DISTINCT id)        AS participant_count,
                 COALESCE(SUM(deposit), 0) AS total_deposit_sum
          FROM DONG.member_challenge
          GROUP BY challenge_id) mc ON c.id = mc.challenge_id
SET c.participants  = mc.participant_count,
    c.total_deposit = mc.total_deposit_sum
WHERE (c.participants != mc.participant_count
    OR c.total_deposit != mc.total_deposit_sum
    OR c.participants IS NULL
    OR c.total_deposit IS NULL);

INSERT INTO quiz (id, question, answer)
VALUES (UNHEX(REPLACE(UUID(), '-', '')), '부채는 기업의 자산에서 빚을 뺀 금액을 의미한다.', FALSE),
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
