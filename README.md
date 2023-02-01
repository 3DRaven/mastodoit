# Введение

Twitter заблочил мое приложение на платформе, так что код не совсем закончен, отдаю как есть, вдруг кому интересно будет.

Но работоспособен!

# Регистрация в сервисах

Процесс регистрации поделен между Pingen и Migrator. 

## Регистрация в Mastodon

![Mastodon diagram](http://www.plantuml.com/plantuml/png/bP7BRi8m44NNy1KZRxffHPvTLB4bNa98zA6GNTL5m0wuTUCeCQfGwR_NJeX299g25xPdhkVuUapv9e-pBSpWkmzwY7Z5X91hZ5QeIRpl2eMHECdW88Y6eyKKHi_XP1fYP24OJnUBEhIhSFao7C-VPaOcPN4zUa4KaY2Q44w3WLxCDlfYJB7yfW1bTb1B0ttqV74MvonLymR1ASW4gdH5kOLq9mGaCeTSIlKVyVC4fciOtwJmmVQW93p6XP9Kff39mfWxadboJ1IGIWNPRYLuiMKdtdjwDhqYf5zUYb99cUxNQzLAm822n28GQdy7Jh5zgD-5pGq7xGdhVXRRE6FcSCxMCgS8tXgQVOR6Ch4tEAV1sINBGBjnJWVElHxdTk2y1kTUYFFEuShFDa3_Ug3VuU--mVaNuYpqa5XmH-glqT5VejCMdPReTebUM_HQeb-AFxy0)

## Регистрация в Twitter

![Twitter diagram](http://www.plantuml.com/plantuml/png/bL8zJyD03DtpAwnE88M4hWnK81Oae9LA1wRebBZfYVOSxfn6yEjnFYvSH9OkKHA_z_pyRDPyH7XlD3DQg9qyLCq7ilf7iIO33hzgD3b6kxPsSm-lkhIAoS8TbCWZbUaJJJGM1RZHfaGZi5HLEZqlK-wXgT0UfCVE7gC14wBNRMsPt2hOwj8ehYq6CDzELAarcjhMd-EiwmzIFGGARbJmlB1J9p0GzpHCB_uDWyfpT2xe6qmjqoJ3A4bEfy67JmrNWUmaZox0o-W-yqTBJSzUL-Bd4WbF_JFmGgKsKAaIP_IxuWpfYU2RQcZQS9Z66q6VfgggrOpS1xePiStp6-HKpDxKM0kZ-xVrVZ_zYCEuPxHwhz5UUONhECmXE82WMxVot6BX9kSGCZE70k2uXMQwY9ryoL-ZJbEF2oSl4kf2PBVgZBy0)

## Авторизованный запрос в Twitter

![Twitter request](http://www.plantuml.com/plantuml/png/RP112y8m38Nl-nN1irftzI285o-Yg4TXb2vqnT7EDbFmrvkvubhmblQzl2y96MeHjZqrpfitrk3nqMCW80VNbo52NnoCzfASXyK6VlKQpgCpasU12wbnnhf6JXaZG7cKTIWcEOy2gHI6K96xevrdhoeWD-UKax4QBVS9sHdnB4N424OEg-mPnb3-R439jeKQW_Acu_QRJSF_ac9woZaEgULo9U5Yj9NKUqnioIAJCR2HpzbyHyccZsTbzswOBHxy0m00)

# Роли пользователей

* Основной, scope read, у него читаются посты в Mastodon и публикуются от его имени в Twitter
* Дополнительный, scope write, посты с ответами из Twitter публикуются как ответы этого пользователя
в Mastodon на посты основного.

Запрашивать твиты без указания даты от какого момента начать выкачивать нельзя потому, что пинген 
следит за частотой запросов так как тогда при каждом запросе нужно спросить хидер. Получает все твиты пинген
от имени пользователя. Пользователь получает хидер, присылая токены и хранит его. Пинген получает 
и передает твиты когда получает хидер (не токены?). Считает сколько твитов получил и этим регулирует число
скачанных юзером твитов. Лимит на число твитов надо сделать подневный, что бы при автивном общении он не резал.

Особо стоит отметить неконсистентеность тредов в Мастодоне. Если в твоем треде идет беседа двух других юзеров,
которые не упоминают тебя и у тебя нет на них подписок. То ты не получишь никаких уведомлений о том, что в твоем треде
происходит какая-то активность. Мало того, даже сервер на котором ты создал юзера может не получить об этом уведомлений
или какой-бы то ни было информации, так как только при наличии подписок они приходят.
