-- Добавляем дефолтную награду за задачу на уровне проекта
ALTER TABLE projects
    ADD COLUMN default_reward_points INT NOT NULL DEFAULT 1;

-- Добавляем награду за конкретную задачу
ALTER TABLE tasks
    ADD COLUMN reward_points INT NOT NULL DEFAULT 1;

-- Добавляем баланс баллов воркера
ALTER TABLE worker_stats
    ADD COLUMN points_balance BIGINT NOT NULL DEFAULT 0,
    ADD COLUMN total_points_earned BIGINT NOT NULL DEFAULT 0;

-- Флаг, что за этот ответ уже выданы баллы
ALTER TABLE answers
    ADD COLUMN reward_granted BOOLEAN NOT NULL DEFAULT FALSE;
