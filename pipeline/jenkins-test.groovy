#!groovy

@Library('my-shared-library')

def infraTools = new jenkins.InfraTools()

infraTools.eventhandler()