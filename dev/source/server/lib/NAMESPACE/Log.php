<?php
/**
 * NAMESPACE Log
 *
 * @category   NAMESPACE
 * @package    NAMESPACE_Log
 * @author     James.Huang <huangjuanshi@snda.com>
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 * @version    $Id$
 */
 
require_once 'Hush/Log.php';

/**
 * @package NAMESPACE_Log
 */
class NAMESPACE_Log
{
	/**
	 * @var string
	 */
	private $_logger = null;
	
	/**
	 * Construct 
	 * Init logger instance
	 * @param string $logger Logger type
	 * @return void
	 */
	public function __construct ($logger = 'sys')
	{
		$this->_logger = Hush_Log::getInstance($logger);
	}
	
	/**
	 * Overload logger log interface
	 * @param string $msg Logging message
	 * @return void
	 */
	public function log ($name, $msg)
	{
		$this->_logger->log($name, $msg);
	}
}
