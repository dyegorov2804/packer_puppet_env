# packer_puppet_env

Environment to create amazon-ebs images using packer and puppet.

# Installing
Make sure you have docker installed

## One time

export AWS_ACCESS_KEY="The key you have"

export AWS_SECRET_KEY="The secret key you have"

# Running

## First Build

To create AWS credentials that will be used for jobs that interface with AWS, set two env variables
before running `docker-compose up`:

    export AWS_ACCESS_KEY=VALID-ACCESS-KEY
    export AWS_SECRET_ACCESS_KEY=VALID-SECRET-ACCESS-KEY

## First Build Continued on Mac OS X

    docker-compose up
    Go to localhost:8080 and run the pipeline. Your custom created AMI should be uploaded to your account.

# Recreate

To recreate from scratch, rerunning all DSL scripts and wiping all settings:

    export AWS_ACCESS_KEY=VALID-ACCESS-KEY
    export AWS_SECRET_ACCESS_KEY=VALID-SECRET-ACCESS-KEY
    docker-compose down
    docker volume rm packer_puppet_env_data-jenkins
    docker-compose build
    # On Mac OS X
    docker-compose up
