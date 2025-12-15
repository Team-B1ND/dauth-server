-- DAuth 기본 애플리케이션 등록
INSERT INTO applications (name, owner_id, client_id, client_secret, url, redirect_url, is_public)
VALUES (
    'DAuth',
    'legolove08',
    'a1b2c3d4-e5f6-7890-abcd-ef1234567890',
    'f9e8d7c6-b5a4-3210-fedc-ba0987654321',
    'https://dauth.b1nd.com',
    'https://dauth.b1nd.com/callback',
    1
);
