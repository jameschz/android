<?php
require_once '../../etc/app.config.php';
require_once 'Hush/App.php';

$app = new Hush_App();

$app->setErrorPage('./404.php')
	->addMapFile(__MAP_INI_FILE)
	->addAppDir(__LIB_PATH_SERVER)
	->addAppDir(__LIB_PATH_WEBSITE);

/**
 * skip 404 page and trace exception
 * TODO : should be commented in www environment
 */
$app->setDebug(true);

$app->run(array(
	'defaultClassSuffix' => 'Server'
));
