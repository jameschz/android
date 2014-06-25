<?php
/**
 * NAMESPACE Util
 *
 * @category   NAMESPACE
 * @package    NAMESPACE_Util
 * @author     James.Huang <huangjuanshi@snda.com>
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 * @version    $Id$
 */

/**
 * @package NAMESPACE_Util
 */
class NAMESPACE_Util_Image
{
	public static function getFaceUrl ($id) 
	{
		$facePath = __HOST_WEBSITE . '/faces/default';
		return $facePath . '/face_' . $id . '.png';
	}
	
	public static function getFaceImage ($id) 
	{
		return array(
			'id' => $id,
			'url' => self::getFaceUrl($id),
			'type' => 'png',
		);
	}
}