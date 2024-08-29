DROP DATABASE IF EXISTS `DONG`;

CREATE DATABASE `DONG`;

USE `DONG`;

CREATE TABLE DONG.`member` (
                               `created_at` datetime(6) DEFAULT NULL,
                               `deleted_at` datetime(6) DEFAULT NULL,
                               `name` varchar(8) NOT NULL,
                               `updated_at` datetime(6) DEFAULT NULL,
                               `nickname` varchar(10) NOT NULL,
                               `id` binary(16) NOT NULL,
                               `device_token` varchar(255) DEFAULT NULL,
                               `email` varchar(255) NOT NULL,
                               `profile_image` varchar(255) DEFAULT NULL,
                               `user_key` varchar(255) DEFAULT NULL,
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


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
                             `description` varchar(255) DEFAULT NULL,
                             `image` varchar(255) DEFAULT NULL,
                             `title` varchar(255) DEFAULT NULL,
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
