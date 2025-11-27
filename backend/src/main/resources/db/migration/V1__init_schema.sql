-- Пользователи
CREATE TABLE users (
                       id              BIGSERIAL PRIMARY KEY,
                       username        VARCHAR(100) NOT NULL UNIQUE,
                       email           VARCHAR(255) NOT NULL UNIQUE,
                       password_hash   VARCHAR(255) NOT NULL,
                       role            VARCHAR(20)  NOT NULL, -- CLIENT / WORKER / ADMIN
                       created_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
                       last_login_at   TIMESTAMP
);

-- Статистика воркера
CREATE TABLE worker_stats (
                              id              BIGSERIAL PRIMARY KEY,
                              worker_id       BIGINT      NOT NULL REFERENCES users(id),
                              tasks_completed INT         NOT NULL DEFAULT 0,
                              avg_score       DOUBLE PRECISION,
                              last_active     TIMESTAMP
);

-- Проект
CREATE TABLE projects (
                          id              BIGSERIAL PRIMARY KEY,
                          name            VARCHAR(255) NOT NULL,
                          description     TEXT,
                          owner_id        BIGINT       NOT NULL REFERENCES users(id),
                          data_type       VARCHAR(50),
                          status          VARCHAR(30)  NOT NULL DEFAULT 'ACTIVE',
                          created_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
                          updated_at      TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- Датасет
CREATE TABLE datasets (
                          id                  BIGSERIAL PRIMARY KEY,
                          project_id          BIGINT      NOT NULL REFERENCES projects(id),
                          file_path           VARCHAR(500) NOT NULL,
                          processed_base_path VARCHAR(500),
                          type                VARCHAR(50),
                          version             INT         NOT NULL DEFAULT 1,
                          status              VARCHAR(30) NOT NULL DEFAULT 'NEW',
                          created_at          TIMESTAMP   NOT NULL DEFAULT NOW(),
                          processed_at        TIMESTAMP
);

-- Задание
CREATE TABLE tasks (
                       id              BIGSERIAL PRIMARY KEY,
                       project_id      BIGINT      NOT NULL REFERENCES projects(id),
                       dataset_id      BIGINT      REFERENCES datasets(id),
                       type            VARCHAR(50),
                       payload_ref     VARCHAR(500),   -- ссылка на конкретный элемент данных
                       input_preview   TEXT,
                       status          VARCHAR(30) NOT NULL DEFAULT 'NEW',
                       assigned_to     BIGINT      REFERENCES users(id),
                       created_at      TIMESTAMP   NOT NULL DEFAULT NOW(),
                       updated_at      TIMESTAMP   NOT NULL DEFAULT NOW()
);

-- Ответ пользователя
CREATE TABLE answers (
                         id               BIGSERIAL PRIMARY KEY,
                         task_id          BIGINT      NOT NULL REFERENCES tasks(id),
                         worker_id        BIGINT      NOT NULL REFERENCES users(id),
                         answer_json      JSONB       NOT NULL,
                         evaluation_score DOUBLE PRECISION,
                         is_accepted      BOOLEAN,
                         created_at       TIMESTAMP   NOT NULL DEFAULT NOW()
);

-- Снимок аналитики по проекту
CREATE TABLE project_analytics_snapshots (
                                             project_id        BIGINT PRIMARY KEY REFERENCES projects(id),
                                             total_tasks       INT       NOT NULL DEFAULT 0,
                                             completed_tasks   INT       NOT NULL DEFAULT 0,
                                             avg_worker_score  DOUBLE PRECISION,
                                             updated_at        TIMESTAMP NOT NULL DEFAULT NOW()
);
