delete from t_user;
alter table t_user auto_increment = 0;

select count(1) from t_user;
select * from t_user;

SELECT * 
FROM t_user 
WHERE LENGTH(key_skills) - LENGTH(REPLACE(key_skills, ',', '')) + 1 = 2;

SELECT * FROM t_user 
WHERE gender = '女' AND age <= 25 
AND (
    key_skills LIKE '%Communication%' OR 
    key_skills LIKE '%Problem solving%' OR 
    key_skills LIKE '%Technical savvy%' OR 
    key_skills LIKE '%Self-motivated%' OR 
    key_skills LIKE '%Confidence%' OR 
    key_skills LIKE '%Teamwork%' OR 
    key_skills LIKE '%Fast learner%'
)
HAVING LENGTH(key_skills) - LENGTH(REPLACE(key_skills, ',', '')) + 1 = 2;


delete from t_eqp;
alter table t_eqp auto_increment = 0;

select count(1) from t_eqp;
select * from t_eqp;