<?php
/**
 * Weibo Util
 *
 * @category   Weibo
 * @package    Weibo_Util
 * @author     James.Huang <huangjuanshi@snda.com>
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 * @version    $Id$
 */

/**
 * @package Weibo_Util
 */
class Weibo_Util_Session
{
	public function __construct() 
	{
		$sid = $_SERVER['HTTP_SID'] ? $_SERVER['HTTP_SID'] : @$_REQUEST['sid'];
		if ($sid) session_id($sid);
		self::autoStart();
	}
	
	static public function autoStart()
	{
		session_start();
		if (!session_id()) {
			Hush_Util::headerRedirect(Weibo_Util_Url::format($_SERVER['REQUEST_URI']));
		}
	}
}