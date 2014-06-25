<?php
/**
 * Weibo Cli
 *
 * @category   Weibo
 * @package    Weibo_Cli
 * @author     James.Huang <huangjuanshi@snda.com>
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 * @version    $Id$
 */

require_once 'Hush/Db/Config.php';

/**
 * @package Weibo_Cli
 */
class Weibo_Cli_Sys extends Weibo_Cli
{
	public function __init ()
	{
		parent::__init();
		
		$this->init_sql = realpath(__ROOT . '/doc/install/mysql.sql');
	}
	
	public function helpAction ()
	{
		// command description
		$this->_printHeader();
		echo "hush sys init\n";
	}
	
	public function initAction () 
	{
		echo 
<<<NOTICE

**********************************************************
* Start to initialize the Hush Framework                 *
**********************************************************

Please pay attention to this action !!!

Because you will do following things :

1. Import original databases (Please make sure your current databases were already backuped).
2. Check all the runtime environment variables and directories.

Are you sure to do all above things [Y/N] : 
NOTICE;
		
		// check user input
		$input = fgets(fopen("php://stdin", "r"));
		if (strcasecmp(trim($input), 'y')) {
			exit;
		}
		
		// import backend and frontend
		$init_cmd = str_replace(
			array('{PARAMS}', '{SQLFILE}'), 
			array($this->_getCmdParams(), $this->init_sql),
			__MYSQL_IMPORT_COMMAND);
		
		echo "\nRun Command : $init_cmd\n";
		system($init_cmd, $res);
		
		if (!$res) {
			echo "Import database ok.\n";
		} else {
			exit;
		}
		
		echo 
<<<NOTICE

**********************************************************
* Initialize successfully                                *
**********************************************************

Thank you for using Hush Framework !!!

NOTICE;
	}
}
