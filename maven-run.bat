echo off
cd %~d0
rem D:
SET PROJECT_HOME=%cd%
ECHO Project HOME:"%PROJECT_HOME%"



:mvn_command
cd %PROJECT_HOME%
ECHO.
ECHO.ע�⣺���ڵı���Ͳ�����ֿ�����Ҫ��ѡ����������ɹ�����ѡ������jetty
ECHO.
ECHO 1-����eclipse�����ļ�
ECHO.
ECHO 2-������
ECHO.
ECHO 0-�˳��˵�

set /p isopt=��ѡ�����
if /i "%isopt%"=="1" goto mvn_eclipse_again
if /i "%isopt%"=="2" goto mvn_full_package

echo "��Чѡ���ѡ��(0-9)"
goto mvn_command

:mvn_eclipse
	ECHO.
	ECHO.
	ECHO.
	ECHO 1-��һ������eclipse�����ļ�
	ECHO.
	ECHO 2-��������eclipse�����ļ�
	ECHO.
	ECHO 0-����
	set /p eopt=��ѡ�����
	if /i "%eopt%"=="1" goto mvn_eclipse_first
	if /i "%eopt%"=="2" goto mvn_eclipse_again
	goto mvn_command

:mvn_eclipse_first
	cd %PROJECT_HOME%\all
	echo ����eclipse�����ļ�
	start /HIGH mvn install eclipse:eclipse -Dmaven.test.skip -Denforcer.skip -Denv=release
	goto mvn_command

:mvn_eclipse_again
	cd %PROJECT_HOME%\all
	echo ����eclipse�����ļ�
	start /HIGH mvn eclipse:clean eclipse:eclipse
	goto mvn_command

	
:mvn_full_package
	cd %PROJECT_HOME%\all
	start /HIGH mvn clean install -Dmaven.test.skip  -DuserProp=%ANTX_PROPERTY%
	goto mvn_command
	
:mvn_end
cd %PROJECT_HOME%
