<?php

/**
 * Common libraries paths
 * TODO : Copy Zend Framework and Hush libraries to following paths !!!
 */
define('__APP_DIR', realpath(dirname(__FILE__) . '/../../'));
define('__COMM_LIB_DIR', realpath(__APP_DIR . '/../phplibs'));
define('__HUSH_LIB_DIR', realpath(__APP_DIR . '/../hush-framework/hush-lib'));
set_include_path('.' . PATH_SEPARATOR . __LIB_DIR . PATH_SEPARATOR . __COMM_LIB_DIR . PATH_SEPARATOR . __HUSH_LIB_DIR . PATH_SEPARATOR . get_include_path());
require_once 'Hush/Util.php';

/**
 * Project Settings
 * TODO : following defines must be set manually !!!
 */
define('__APP_NAME', 'testapp');

/**
 * Script Settings
 */
$clientSourceDir = __APP_DIR . '/dev/source/client';
$clientTargetDir = __APP_DIR . '/app/' . __APP_NAME . '/client';
$serverSourceDir = __APP_DIR . '/dev/source/server';
$serverTargetDir = __APP_DIR . '/app/' . __APP_NAME . '/server';
define('__APP_ROOT', str_replace('\\','/', $serverTargetDir));