module "db_default" {
  source = "../infra/infraRds"
  aws_region = "us-east-1"
  identifier = "group4-rds"
  allocated_storage = 20
  engine = "postgres"
  engine_version = "14.2"
  instance_class = "db.t3.micro"
  name = "mypostgres"
  username = "group4"
  password = "Perficient2022*"
  port = 5432
}