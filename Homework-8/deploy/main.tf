module "deploy_ec2" {
  source        = "../infra"
  aws_region    = "us-east-1"
  instance_type = "t2.small"
  ec2_tags      = { Name = "group4_ec2" }
  ami_id        = "ami-005de95e8ff495156"
  key_pair_name = "grupo4-key"
  user_data     = file("../infra/user_data.sh")

  eip_tags      = { Name = "group4_eip" }

  subnet_id = "subnet-04e972f3a706c00e8"
  public_ip = false

  sg_name        = "group4-sg"
  sg_description = "Allow port 8080 for jenkins, ssh over port 22 only for host ip, port 4200 frontend, port 5432 for postgres"
  vpc_id         = "vpc-031420f7c99b1a0bd"
  host_ip        = "192.168.0.133/24"
  sg_tags        = { Name = "group4-sg" }

  identifier = "group4-rds"
  allocated_storage = 20
  engine = "postgres"
  engine_version = "14.2"
  instance_class = "db.t3.micro"
  name = "my-postgres"
  username = "group4"
  password = "Perficient2022*"
  port = 5432
  skip_final_snapshot = true
  publicly_accessible = true
}