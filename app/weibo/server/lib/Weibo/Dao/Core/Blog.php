<?php
/**
 * Weibo Dao
 *
 * @category   Weibo
 * @package    Weibo_Dao_Core
 * @author     James.Huang <shagoo@gmail.com>
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 * @version    $Id$
 */

require_once 'Weibo/Dao/Core.php';
require_once 'Weibo/Dao/Core/Customer.php';
require_once 'Weibo/Util/Image.php';

/**
 * @package Weibo_Dao_Core
 */
class Core_Blog extends Weibo_Dao_Core
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
	 * @param int $startId Start Blog ID
	 */
	public function getListByPage ($startId = 0)
	{
		$list = array();
		$sql = $this->select()
			->from($this->t1, '*');
		if ($startId > 0) {
			$sql = $sql->where("{$this->t1}.id > ?", $startId);
		}
		$sql = $sql->order("{$this->t1}.uptime desc");
		$sql = $sql->limit(5);
		
		$res = $this->dbr()->fetchAll($sql);
		foreach ($res as $row) {
			$customerDao = new Core_Customer();
			$customer = $customerDao->read($row['customerid']);
			$blog = array(
				'id'		=> $row['id'],
				'face'		=> Weibo_Util_Image::getFaceUrl($customer['face']),
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
	 * @param int $startId Start Blog ID
	 */
	public function getListByCustomer ($customerId, $startId = 0)
	{
		$list = array();
		$sql = $this->select()
			->from($this->t1, '*')
			->where("{$this->t1}.customerid = ?", $customerId);
		if ($startId > 0) {
			$sql = $sql->where("{$this->t1}.id > ?", $startId);
		}
		$sql = $sql->order("{$this->t1}.uptime desc");
		$sql = $sql->limit(5);
		
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