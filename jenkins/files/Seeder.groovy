pipelineJob('packer-templates-seed') {
    definition {
        parameters {
            stringParam('PACKER_LOG', '0')
            stringParam('BUILD_TYPE', 'amazon-ebs')
        }
        cpsScm {
            scm {
                git {
                remote {
                    url('https://github.com/dyegorov2804/packer_puppet.git')
                }
                branch('master')
                }
                scriptPath('Jenkinsfile')
            }
        }
    }
}