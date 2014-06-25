<?php

$buildRoot = dirname(__FILE__);
$buildCommand = isset($argv[1]) ? trim($argv[1]) : '';
$buildScript = $buildRoot . '/script/' . $buildCommand . '.php';
if (!file_exists($buildScript)) {
	echo
<<<USAGE
Usage: build [all|server|client]
USAGE;
	exit;
} else {
	system("php " . $buildScript);
}