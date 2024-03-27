insert into client_app (client_id,client_secret,duration_in_minutes,required_proof_key) values ('client','$2a$10$QHLrXJ8nlnDlOXEDUnV6iefGNOkKaW9pqJwZcpF.5HrO42PRPzjcO',10,false);
insert into client_app_authorization_grant_types (client_app_id,authorization_grant_types) values ((select id from client_app where client_id = 'client'),'authorization_code');
insert into client_app_authorization_grant_types (client_app_id,authorization_grant_types) values ((select id from client_app where client_id = 'client'),'refresh_token');
insert into client_app_authorization_grant_types (client_app_id,authorization_grant_types) values ((select id from client_app where client_id = 'client'),'client_credentials');
insert into client_app_client_authentication_methods (client_app_id,client_authentication_methods) values ((select id from client_app where client_id = 'client'),'client_secret_basic');
insert into client_app_redirect_uris (client_app_id,redirect_uris) values ((select id from client_app where client_id = 'client'),'https://oauthdebugger.com/debug');
insert into client_app_scopes (client_app_id,scopes) values ((select id from client_app where client_id = 'client'),'openid');
insert into client_app_scopes (client_app_id,scopes) values ((select id from client_app where client_id = 'client'),'READ_ALL_PRODUCTS');
insert into client_app_scopes (client_app_id,scopes) values ((select id from client_app where client_id = 'client'),'READ_ONE_PRODUCT');


insert into client_app (client_id,client_secret,duration_in_minutes,required_proof_key) values ('my-own-client','$2a$10$QHLrXJ8nlnDlOXEDUnV6iefGNOkKaW9pqJwZcpF.5HrO42PRPzjcO',10,true);
insert into client_app_authorization_grant_types (client_app_id,authorization_grant_types) values ((select id from client_app where client_id = 'my-own-client'),'authorization_code');
insert into client_app_authorization_grant_types (client_app_id,authorization_grant_types) values ((select id from client_app where client_id = 'my-own-client'),'refresh_token');
insert into client_app_authorization_grant_types (client_app_id,authorization_grant_types) values ((select id from client_app where client_id = 'my-own-client'),'client_credentials');
insert into client_app_client_authentication_methods (client_app_id,client_authentication_methods) values ((select id from client_app where client_id = 'my-own-client'),'client_secret_basic');
insert into client_app_redirect_uris (client_app_id,redirect_uris) values ((select id from client_app where client_id = 'my-own-client'),'https://oauthdebugger.com/debug');
insert into client_app_scopes (client_app_id,scopes) values ((select id from client_app where client_id = 'my-own-client'),'openid');
insert into client_app_scopes (client_app_id,scopes) values ((select id from client_app where client_id = 'my-own-client'),'ALL');
