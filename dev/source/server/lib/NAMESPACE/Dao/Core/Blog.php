<?php
/**
 * NAMESPACE Dao
 *
 * @category   NAMESPACE
 * @package    NAMESPACE_Dao_Core
 * @author     James.Huang <shagoo@gmail.com>
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 * @version    $Id$
 */

require_once 'NAMESPACE/Dao/Core.php';
require_once 'NAMESPACE/Dao/Core/Customer.php';
require_once 'NAMESPACE/Util/Image.php';

/**
 * @package NAMESPACE_Dao_Core
 */
class Core_Blog extends NAMESPACE_Dao_Core
{
	/**
	 * @static
	 */
	const TABLE_NAME = 'blog';
	
	/**
	 * @static
	 */
	const TABLE_PRIM = 'id';
	
	/**
	 * Initialize
	 */
	public function __init () 
	{
		$this->t1 = self::TABLE_NAME;
		$this->k1 = self::TABLE_PRIM;
		
		$this->_bindTable($this->t1, $this->k1);
	}
	
	/**
	 * Add commentcount by one
	 * @param int $id
	 */
	public function addCommentcount ($id, $addCount = 1)
	{
		$blog = $this->read($id);
		$blog['commentcount'] = $blog['commentcount'] + $addCount;
		$this->update($blog);
	}
	
	/**
	 * Get all blog list 
	 * @param int $pageId
	 */
	public function getListByPage ($pageId = 0)
	{
		$list = array();
		$sql = $this->select()
			->from($this->t1, '*')
			->order("{$this->t1}.uptime desc")
			->limit(10);
		
		$res = $this->dbr()->fetchAll($sql);
		foreach ($res as $row) {
			$customerDao = new Core_Customer();
			$customer = $customerDao->read($row['customerid']);
			$blog = array(
				'id'		=> $row['id'],
				'face'		=> NAMESPACE_Util_Image::getFaceUrl($customer['face']),
				'content'	=> '<b>'.$customer['name'].'</b> : '.$row['content'],
				'comment'	=> '评论('.$row['commentcount'].')',
				'uptime'	=> $row['uptime'],
			);
			array_push($list, $blog);
		}
		return $list;
	}
	
	/**
	 * Get blog list 
	 * @param string $customerId Customer ID
	 * @param int $pageId
	 */
	public function getListByCustomer ($customerId, $pageId = 0)
	{
		$list = array();
		$sql = $this->select()
			->from($this->t1, '*')
			->where("{$this->t1}.customerid = ?", $customerId)
			->order("{$this->t1}.uptime desc");
		
		$res = $this->dbr()->fetchAll($sql);
		foreach ($res as $row) {
			$customerDao = new Core_Customer();
			$customer = $customerDao->read($row['customerid']);
			$blog = array(
				'id'		=> $row['id'],
				'content'	=> '<b>'.$customer['name'].'</b> : '.$row['content'],
				'comment'	=> '评论('.$row['commentcount'].')',
				'uptime'	=> $row['uptime'],
			);
			array_push($list, $blog);
		}
		return $list;
	}
}