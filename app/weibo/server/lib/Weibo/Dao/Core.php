<?php
/**
 * Weibo Dao
 *
 * @category   Weibo
 * @package    Weibo_Dao
 * @author     James.Huang <shagoo@gmail.com>
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 * @version    $Id$
 */
 
require_once 'Weibo/Dao.php';

/**
 * @package Weibo_Dao
 */
class Weibo_Dao_Core extends Weibo_Dao
{
	/**
	 * @static
	 */
	const DB_NAME = 'weibo_core';
	
	/**
	 * Construct
	 */
	public function __construct ()
	{
		// initialize dao
		parent::__construct(MysqlConfig::getInstance());
		
		// set default dao settings
		$this->_bindDb(self::DB_NAME);
	}
}