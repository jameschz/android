<?php
/**
 * NAMESPACE Dao
 *
 * @category   NAMESPACE
 * @package    NAMESPACE_Dao
 * @author     James.Huang <shagoo@gmail.com>
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 * @version    $Id$
 */
 
require_once 'NAMESPACE/Dao.php';

/**
 * @package NAMESPACE_Dao
 */
class NAMESPACE_Dao_Core extends NAMESPACE_Dao
{
	/**
	 * @static
	 */
	const DB_NAME = 'NAMESPACE_core';
	
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