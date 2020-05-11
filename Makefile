start_network:
	docker-compose -f base.yml up --remove-orphans --force-recreate --build  -d
start_base:
	docker-compose -p smzone_base -f base.yml up --remove-orphans --force-recreate --build  -d
start_hello:
	docker-compose -p smzone_hello -f hello.yml up --remove-orphans --force-recreate --build  -d
start_iamapi:
	docker-compose -p smzone_iamapi -f iam.yml up --remove-orphans --force-recreate --build  -d
start_msgapi:
	docker-compose -p smzone_msgapi -f msg.yml up --remove-orphans --force-recreate --build  -d
start_dev: # 开启开发服务
	docker-compose -f dev.yml up --remove-orphans --force-recreate --build  -d 

start_openjdk: # 开启开发服务
	docker-compose -f openjdk.yml up --remove-orphans --force-recreate --build  -d 
