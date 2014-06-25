<?php

require_once dirname(__FILE__) . '/config.php';

////////////////////////////////////////////////////////////////////////////////////////////////////
// 

echo "\n**********************************************\n";
echo "* Building Server Code ..                    *\n";
echo "**********************************************\n";

// replace app's name and namespace
function dir_copy_wrapper ($fromFile, $destFile) {
	// load settings
	$defaultNs = 'NAMESPACE';
	if (preg_match('/.sql$/i', $destFile) ||
		preg_match('/.project$/i', $destFile)) {
		$appNs = strtolower(__APP_NAME);
	} else {
		$appNs = ucfirst(strtolower(__APP_NAME));
	}
	// replace code
	if (is_file($destFile) && (
		preg_match('/.txt$/i', $destFile) ||
		preg_match('/.ini$/i', $destFile) ||
		preg_match('/.php$/i', $destFile) ||
		preg_match('/.sql$/i', $destFile) ||
		preg_match('/.project$/i', $destFile))) {
		echo "FILE $destFile ... ";
		$destFileContent = file_get_contents($destFile);
		$destFileContent = str_replace($defaultNs, $appNs, $destFileContent);
		file_put_contents($destFile, $destFileContent, LOCK_EX);
		echo "ok.\n";
		return;
	}
	// replace httpd conf
	if (is_file($destFile) && preg_match('/.conf$/i', $destFile)) {
		echo "FILE $destFile ... ";
		$destFileContent = file_get_contents($destFile);
		$destFileContent = str_replace(array('APPNAME', 'APPROOT'), array(__APP_NAME, __APP_ROOT), $destFileContent);
		file_put_contents($destFile, $destFileContent, LOCK_EX);
		echo "ok.\n";
		return;
	}
	// change dir name
	if (is_dir($destFile) && preg_match("/{$defaultNs}$/i", $destFile)) {
		echo "FILE $destFile ... \n";
		$destDir = preg_replace("/{$defaultNs}$/", $appNs, $destFile);
		echo "\nDIR $destFile > $destDir";
		echo "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n";
		Hush_Util::dir_copy($destFile, $destDir);
		Hush_Util::dir_remove($destFile);
		echo "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n";
		echo "ok.\n\n";
		return;
	}
}

echo "\nDIR $serverSourceDir > $serverTargetDir";
echo "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n";
Hush_Util::dir_remove($serverTargetDir);
Hush_Util::dir_copy($serverSourceDir, $serverTargetDir);
echo "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n";
echo "ok.\n\n";