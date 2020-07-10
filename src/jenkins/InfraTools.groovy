def checkoutRepo(){

	stage('Checkout Repo') {
		checkout([$class: 'GitSCM', 
        branches: [[name: '*/testing_python']], 
        doGenerateSubmoduleConfigurations: false, 
        extensions: [], 
        submoduleCfg: [], 
        userRemoteConfigs: [[credentialsId: '918483d2-a278-43f7-8618-1dc618466dfc', url: 'https://github.com/vinu1421/jenkins_test.git']]
        
        ])
	}

}

def parameter(){

    properties([
    [
        $class: 'JiraProjectProperty'
    ], 
    
   
        
        [
            $class: 'ExtensibleChoiceParameterDefinition',
            choiceListProvider: [$class: 'TextareaChoiceListProvider', 
            addEditedValue: false, 
            choiceListText: '''DoNothing\nEnable\nDisable''',
            defaultChoice: 'Enable'], 
            description: '', 
            editable: false, 
            name: 'EventHandlerAction'
        ],
        
        [
            $class: 'CascadeChoiceParameter', 
            choiceType: 'PT_CHECKBOX', 
            description: '', 
            filterLength: 1, 
            filterable: false, 
            name: 'ServiceName', 
            randomName: 'choice-parameter-38182949408198', 
            referencedParameters: 'EventHandlerAction', 
            script: [
                $class: 'GroovyScript', 
                fallbackScript: [classpath: [], 
                sandbox: false, script: ''], 
                script: [classpath: [], 
                sandbox: false, 
                script: '''if (EventHandlerAction.equals("Enable") || EventHandlerAction.equals("Disable")) {\nreturn ["check_jboss_gc","check_ajp_avg_request_rejected_count","check_ATGProductionDS_session_count","check_mdex_load_restartWA"]\n}''']
                
            ]
        ],
         

        
        [
            $class: 'CascadeChoiceParameter', 
            choiceType: 'PT_CHECKBOX', 
            description: '', 
            filterLength: 1, 
            filterable: false, 
            name: 'C4Environment', 
            randomName: 'choice-parameter-38182959825985', 
            referencedParameters: 'EventHandlerAction',
            script: [
                $class: 'GroovyScript', 
                fallbackScript: [classpath: [], 
                sandbox: false, 
                script: ''], 
                script: [classpath: [], 
                sandbox: false, 
                script: '''if (EventHandlerAction.equals("Enable") || EventHandlerAction.equals("Disable")) {\nreturn ["FOOD-PRODUCTION","NONFOOD-PRODUCTION","ATUIN-Microservices-PROD"]\n}''']
            ]
            
        ], 
        
        [
            $class: 'CascadeChoiceParameter', 
            choiceType: 'PT_CHECKBOX', 
            description: '', 
            filterLength: 1, 
            filterable: false, 
            name: 'EventHandler', 
            randomName: 'choice-parameter-38182962295796', 
            referencedParameters: 'ServiceName,C4Environment', 
            script: [
                $class: 'GroovyScript', 
                fallbackScript: [classpath: [], 
                sandbox: false, 
                script: ''], 
                script: [classpath: [], 
                sandbox: false, 
                script: 'return ["sf_restart","ATUIN_OrderCount_Escalations","check_ds_session","mdex_load_eventhandler"]']
                
                ]
        ]
        

    ])
])
}

def eventhandler() {

    parameter()
    node() {



        stage('Build') { 
        List servers = "${C4Environment}".split(',')

        checkoutRepo()
        sh 'python3 newpython.py'
    }

}


}