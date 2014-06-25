<?php

require_once dirname(__FILE__) . '/config.php';

////////////////////////////////////////////////////////////////////////////////////////////////////
// 

echo "\n**********************************************\n";
echo "* Building Client Code ..                    *\n";
echo "**********************************************\n";

// replace app's name and namespace
function dir_copy_wrapper ($fromFile, $destFile) {
	// load settings
	$defaultNs = 'NAMESPACE';
	$appNs = strtolower(__APP_NAME);
	// replace code
	if (is_file($destFile) && (
		preg_match('/.txt$/i', $destFile) ||
		preg_match('/.ini$/i', $destFile) ||
		preg_match('/.xml$/i', $destFile) ||
		preg_match('/.java$/i', $destFile) ||
		preg_match('/.project$/i', $destFile) ||
		preg_match('/.properties/i', $destFile))) {
		echo "FILE $destFile ... ";
		$destFileContent = file_get_contents($destFile);
		$destFileContent = str_replace($defaultNs, $appNs, $destFileContent);
		file_put_contents($destFile, $destFileContent, LOCK_EX);
		echo "ok.\n";
		return;
	}
	// change dir name
	if (is_dir($destFile) && preg_match("/{$defaultNs}$/i", $destFile)) {
		echo "FILE1 $destFile ... \n";
		$destDir = preg_replace("/{$defaultNs}$/", $appNs, $destFile);
		echo "\nDIR1 $destFile > $destDir";
		echo "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n";
		Hush_Util::dir_copy($destFile, $destDir);
		Hush_Util::dir_remove($destFile);
		echo "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n";
		echo "ok.\n\n";
		return;
	}
}

echo "\nDIR $clientSourceDir > $clientTargetDir";
echo "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n";
Hush_Util::dir_remove($clientTargetDir);
Hush_Util::dir_copy($clientSourceDir, $clientTargetDir);
echo "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n";
echo "ok.\n\n";