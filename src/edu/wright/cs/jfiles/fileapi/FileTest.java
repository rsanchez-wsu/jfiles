/*
 * Copyright (C) 2016 - WSU CEG3120 Students
 *
 * Roberto C. Sánchez <roberto.sanchez@wright.edu>
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package edu.wright.cs.jfiles.fileapi;

import java.io.File;
import java.util.Scanner;

/**
 * This is a functional class that contains methods for testing the functions in
 * the JFileManager (JFM) and in JFile. Where applicable, these methods should
 * use files made explicitly for testing purposes and return them to heir
 * original state at the end of the method.
 *
 * <p>
 * This class' methods also should have the initialize method called in all
 * exception calls in case the test fails and a file is left renamed, not
 * deleted, or not replaced after deletion.
 * </p>
 * @author John Wintersohle II
 * @author Randy Musser
 *
 */
public class FileTest {

	/**
	 * <b>NOTE</b>: this method should always be ran after testDelete and
	 * testCopy. This is because the cut method just calls these two methods, so
	 * the primary purpose of this method should be to test how the cut method
	 * puts the two together.
	 *
	 * <p>
	 * This method shall test cut method from the JFM. As opposed to testing the
	 * copy and delete methods independently, this method tests the final result
	 * of the cut command. This is to make sure the tester does not assume that,
	 * because the methods that make up the cut method work that the final
	 * product will work.
	 * </p>
	 * <p>
	 * That is to say, if the tester know that copy and delete are already
	 * working correctly, but cut is not, they can know that the problem' cause
	 * is in how the cut method puts the two methods together.
	 * </p>
	 */
	public static void testCut() {

	}

	/**
	 * This method shall copy an array of test files that comes with the program
	 * to the clipboard and display the results in the console. This method
	 * should then use the compareFiles method to verify that the files in the
	 * clipboard have the same contents as the files in the system.
	 *
	 * <p>
	 * After copying to the clipboard, the system shall remove the contents from
	 * the clipboard so that the system is returned to its starting state.
	 * </p>
	 */
	public static void testCopy(JFile testFile, String placeToPasteTo) {
		JFile[] tempJfArr = {testFile};
		JFileManager.copy(tempJfArr);
		JFileManager.paste(placeToPasteTo);
	}

	/**
	 * This method shall arbitrarily assign an array of test files to the
	 * clipboard and then test the paste method by calling it and using the
	 * compareFiles method to verify that the files are the same.
	 *
	 * <p>
	 * After the file is created, the file shall be deleted to return the system
	 * to its starting state.
	 * </p>
	 */
	public static void testRegPaste() {

	}

	/**
	 * <b>NOTE</b> This method MUST call the testDelete method before testing
	 * the actual move method. This is because, since the paste method in move
	 * is private and needs to stay private, we cannot access this form of the
	 * paste method outside of the move method. This, in turn, means that we
	 * need to come up with a way of singling out the this part of the move
	 * method. This method tests the final result of the move method, but also
	 * functions as the test for that version of the paste method.
	 *
	 * <p>
	 * -------------------------------------------------------------------------
	 * </p>
	 * <p>
	 * This tests both the move method of JFileManager as well as the version of
	 * paste that it uses. After copying the file, this method shall call the
	 * compareFiles method to ensure that the contents of the files are the same
	 * before delete the original files. From there, this method shall delete
	 * the original files and make sure that they are deleted from the
	 * filesystem.
	 * </p>
	 * <p>
	 * Since the move method functionally consists of just a call to the special
	 * paste method for move followed by the normal delete method, if we test
	 * the delete method first and the testMove method fails, we can know that
	 * it is the paste method that has the problem.
	 * </p>
	 * <p>
	 * If the system was able to move the file to its new location, the system
	 * shall attempt to move it back to prepare for the next call of this
	 * method.
	 * </p>
	 */
	public static void testMove() {

	}

	/**
	 * This method shall test the delete method of JFileManager. After deleting
	 * the files, this method shall verify that these files are deleted. After
	 * verifying that the files are deleted, the system shall replace the files
	 * so that the system can be ready for the next time it is called. The
	 * system shall also verify that the file was successfully replaced. Though
	 * this reflects on the testing software, not the actual JFM, this should
	 * still be checked.
	 *
	 * <p>
	 * If the file used for testing this method is already deleted from some
	 * reason, the file will be made and then the test will be ran. With that
	 * said, the fact that it was missing should be noted in the log. This is to
	 * ensure that the tester knows that something is wrong with the testing
	 * software.
	 * </p>
	 */
	public static void testDelete(JFile[] file) {
		JFileManager.delete(file);
	}

	/**
	 * This method shall test the JFM's rename method. It shall do this by
	 * recording what the files name is before changing it, then comparing it to
	 * the name of the file after changing it. The system shall then attempt to
	 * change the name of the file back to prepare the file for the next call to
	 * this method.
	 */
	public static void testRename() {

	}

	/**
	 * This method shall test the default open method by opening a file using
	 * its default program. It is important to note that there is some
	 * difficulty in testing this in that the user may have changed what the
	 * defaults are. To fix this, we shall have a custom file "type" that is
	 * registered to be opened with some application that is guaranteed to be on
	 * any system. The only thing that fits this is JFiles itself, so this
	 * method shall use JFiles until further notice.
	 *
	 * <p>
	 * To make sure the connection to the correct file is tested, this custom
	 * file type must be on the same configuration file that all of the other
	 * file types are on, but it must not be changeable by the user. To ensure
	 * this, the method that sets default programs for different file types
	 * shall check to see if the file type being changed is this custom type and
	 * prevent the user from changing this entry. Additionally, this entry must
	 * not be viewable by the user. This is a technical detail that we want to
	 * hide from the people using this API.
	 * </p>
	 * <p>
	 * Our other option is to make sure we have some other application is
	 * installed as a prerequisite so that we have something to test this
	 * method. For the sake of proper testing this may be wise, but this
	 * application should be small and take up as little space as possible
	 * </p>
	 */
	public static void testOpen() {

	}

	/**
	 * This method shall test the openWith method in the JFM. This shall do so
	 * by taking in a file of a definite type and passing it into a program that
	 * is both guaranteed to be on any system and known to open a specific kind
	 * of file. This can either be JFiles itself, which creates issues in
	 * testing dependency, or a separate program that is explicitly made a
	 * prerequisite of this program.
	 */
	public static void testOpenWith() {

	}

	/**
	 * This method shall test the getType method of the JFM. It shall do this by
	 * calling the method an a few different types that are considered to be
	 * known. This method shall check the output of these method calls against
	 * the expected results and return if and which files showed
	 * inconsistencies.
	 */
	public static void testGetType() {

	}

	/**
	 * This confirm that the contents of two arrays of files are the same. This
	 * may be done by a separate method that goes character by character and
	 * checks to see if they are equal. If a better way is known, it shall be
	 * used and the doc updated to reflect the change.
	 *
	 * <p>
	 * The order of which file is first and second should not be relevant to the
	 * result.
	 * </p>
	 * @param files1
	 *            The first array of files being compared.
	 * @param files2
	 *            The second array of files file being compared.
	 */
	public static boolean compareFiles(JFile[] files1, JFile[] files2) {
		return false;
	}

	/**
	 * This method shall return all files used in testing to their original
	 * state so that testing can be done. This should be done frequently during
	 * testing in case any testing method fail and fail to return the file to
	 * its original state before exiting. This should also make sure all files
	 * are named correctly, look for files that were created and failed to get
	 * deleted, clear the clipboard. This should run in all exceptions.
	 */
	public static void initialize() {

	}

	/**
	 * Place holder text.
	 * @param args Place holder text.
	 */
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		String tmp;

		System.out.println("Please enter the absolute path to a file that you want to copy");
		System.out.println("Remeber to escape the backslash if you are on windows:");
		tmp = in.nextLine();
		System.out.println("Please enter an absolute path to paste the file to:");
		testCopy(new JFile(new File(tmp)), in.nextLine());
		System.out.println("****************************************************");
		System.out.println("Copy & paste test finished.");
		System.out.println("****************************************************");

		JFile[] tmpArr = {null, null};
		System.out.println("\nPlease enter the absolute path to a file that you want to delete");
		System.out.println("Remeber to escape the backslash if you are on windows:");
		tmpArr[0] = new JFile(in.nextLine());
		System.out.println("Please enter another absolute path to a file that you want to delete");
		System.out.println("Remeber to escape the backslash if you are on windows:");
		tmpArr[1] = new JFile(in.nextLine());
		testDelete(tmpArr);
		System.out.println("****************************************************");
		System.out.println("Delete test finished.");
		System.out.println("****************************************************");

		in.close();
	}
}
